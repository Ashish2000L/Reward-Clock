package com.example.rewardclock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.example.rewardclock.broadcasts.AlarmReceiver;
import com.example.rewardclock.database.RoomAlarmDatabase;
import com.example.rewardclock.utils.setAlarm;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.example.rewardclock.broadcasts.AlarmReceiver.ringtone;
import static com.example.rewardclock.splash.SHARED_PREF;
import static com.example.rewardclock.utils.setAlarm.CURRENT_ALARM_ID;

public class alarm_wakeup_screen extends AppCompatActivity {

    SwipeButton swipeButton;
    setAlarm setAlarm = new setAlarm();
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(Window.FEATURE_NO_TITLE| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_alarm_wakeup_screen);

        sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);


//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        swipeButton = findViewById(R.id.swipe_btn);

        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {

//                PendingIntent pendingIntent = PendingIntent.getBroadcast(alarm_wakeup_screen.this,0,new Intent(alarm_wakeup_screen.this, AlarmReceiver.class),PendingIntent.FLAG_NO_CREATE);
//                if (pendingIntent!=null)
//                    alarmManager.cancel(pendingIntent);

                ringtone.stop();

                Toast.makeText(alarm_wakeup_screen.this, "Found ID : "+sharedPreferences.getInt(CURRENT_ALARM_ID,0), Toast.LENGTH_SHORT).show();

                new RoomAlarmDatabase.UpdateAlarmOnOffById(sharedPreferences.getInt(CURRENT_ALARM_ID,0),false);

                try {
                    setAlarm.setNewAlarm(alarm_wakeup_screen.this,-1);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(getApplicationContext(),base_activity.class));
            }
        });

    }
}