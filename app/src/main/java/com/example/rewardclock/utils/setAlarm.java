package com.example.rewardclock.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.rewardclock.broadcasts.AlarmReceiver;
import com.example.rewardclock.database.RoomAlarmDatabase;
import com.example.rewardclock.database.entities.Alarms;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import static android.content.Context.PRINT_SERVICE;
import static com.example.rewardclock.splash.SHARED_PREF;

public class setAlarm {

    public static final String CURRENT_ALARM_ID="current_alarm",IS_SNOOZE_RUNNING="snooze_running", UPCOMING_SNOOZE_ID="next_snooze_id",
    UPCOMING_SNOOZE_TIMESTAMP="next_snooze_millis_time", SNOOZE_REFERENCE="snooze_on_original_alarm_id",SNOOZE_DELAY="snooze_delay";

    public static AlarmManager alarmManager;
    public static PendingIntent pendingIntent;
    SharedPreferences sharedPreferences;

    public setAlarm() {
    }

    public Boolean setNewAlarm(Context context, int id) throws ExecutionException, InterruptedException {
            Alarms alarms;
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if(id>-1) {
                alarms = new RoomAlarmDatabase.SelectOneAlarmById(id).execute(context).get();
            }else{
                int HOUR,MINUTE;
                Calendar calendar = Calendar.getInstance();

                HOUR =calendar.getTime().getHours();
                MINUTE =calendar.getTime().getMinutes();

                alarms = new RoomAlarmDatabase.GetUpcomingAlarms(Integer.parseInt((int)(HOUR/10)+""+(int)(HOUR%10)+""+
                        (int)(MINUTE/10)+""+(int)(MINUTE%10))).execute(context).get();
                if (alarms ==null){
                    Toast.makeText(context, "Null Found!", Toast.LENGTH_SHORT).show();
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,new Intent(context, AlarmReceiver.class),PendingIntent.FLAG_NO_CREATE);
                    if (pendingIntent!=null)
                        alarmManager.cancel(pendingIntent);
                    return false;
                }
                Log.d("setAlarm", "setNewAlarm: alarm found is "+alarms.toString());
            }

            sharedPreferences = context.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        if (sharedPreferences.getLong(CURRENT_ALARM_ID,0)!=alarms.getId()) {

            Toast.makeText(context, "Alarm set for "+alarms.getHour()+":"+alarms.getMinute()+" "+ (alarms.getAmpm()? "AM":"PM"), Toast.LENGTH_SHORT).show();

            Log.d("setAlarm", "Alarm set for "+alarms.getHour()+":"+alarms.getMinute()+" "+ (alarms.getAmpm()? "AM":"PM"));

            Intent intent = new Intent(context, AlarmReceiver.class);

            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Alarm rings continuously until toggle button is turned off
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarms.getTimestamp(), 5*60000, pendingIntent);

            SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).edit();
            editor.putLong(CURRENT_ALARM_ID, alarms.getTimestamp());
            editor.apply();
        }else{
            Log.d("setAlarm", "setNewAlarm: Alarm is already set and need not to change"+alarms.getHour()+":"+alarms.getMinute()+" "+ (alarms.getAmpm()? "AM":"PM"));
        }
        return true;
    }

    public void setSnooze(Context context,int delay){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        int HOUR,MINUTE,SNOOZEID, CURRENTID;
        HOUR =calendar.getTime().getHours();
        MINUTE =calendar.getTime().getMinutes();
        CURRENTID = Integer.parseInt((int)(HOUR/10)+""+(int)(HOUR%10)+""+
                (int)(MINUTE/10)+""+(int)(MINUTE%10));

        calendar.setTimeInMillis(calendar.getTimeInMillis()+delay*60*1000);

        HOUR =calendar.getTime().getHours();
        MINUTE =calendar.getTime().getMinutes();
        SNOOZEID = Integer.parseInt((int)(HOUR/10)+""+(int)(HOUR%10)+""+
                (int)(MINUTE/10)+""+(int)(MINUTE%10));

        try {
            if(!new RoomAlarmDatabase.IsAlarmExistsInSnoozeTime(CURRENTID,SNOOZEID).execute(context).get() && !sharedPreferences.getBoolean(IS_SNOOZE_RUNNING,false)){
                SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE).edit();
                editor.putBoolean(IS_SNOOZE_RUNNING,true);
                editor.putInt(UPCOMING_SNOOZE_ID,SNOOZEID);
                editor.putLong(UPCOMING_SNOOZE_TIMESTAMP,calendar.getTimeInMillis());
                editor.putInt(SNOOZE_REFERENCE,sharedPreferences.getInt(CURRENT_ALARM_ID,-1));
                editor.putInt(SNOOZE_DELAY,delay);
                editor.apply();

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, pendingIntent);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
