package com.example.rewardclock.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.rtp.AudioStream;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.rewardclock.BuildConfig;
import com.example.rewardclock.R;
import com.example.rewardclock.alarm_wakeup_screen;
import com.example.rewardclock.utils.WakeLocker;
import com.example.rewardclock.utils.setAlarm;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

import static com.example.rewardclock.splash.SHARED_PREF;
import static com.example.rewardclock.utils.setAlarm.IS_SNOOZE_RUNNING;
import static com.example.rewardclock.utils.setAlarm.SNOOZE_DELAY;

public class AlarmReceiver extends BroadcastReceiver {

    public static MediaPlayer ringtone;
    setAlarm setAlarm = new setAlarm();
    Handler handler = new Handler();
    SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        WakeLocker.acquire(context);
        Log.d("alarmreciever", "onReceive: called reciever!");
        sharedPreferences = context.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(4000);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (!isEmulator() & !BuildConfig.DEBUG) {
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }else {
                Toast.makeText(context,"Emulator detected!",Toast.LENGTH_LONG).show();
                alarmUri = Uri.parse("android.resource://com.example.rewardclock/" + R.raw.custom_alarm_tone);
            }


//        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone = MediaPlayer.create(context,alarmUri);

        ringtone.setLooping(true);
        ringtone.setVolume(1f,1f);

        ringtone.start();
        context.startActivity(new Intent(context, alarm_wakeup_screen.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        WakeLocker.release();

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    if (!ringtone.isPlaying()) {
//                        try {
//                            setAlarm.setNewAlarm(context,-1);
//                            Toast.makeText(context, "New alarm is set", Toast.LENGTH_SHORT).show();
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }else
//                        if (!sharedPreferences.getBoolean(IS_SNOOZE_RUNNING,false)){
//                            setAlarm.setSnooze(context,sharedPreferences.getInt(SNOOZE_DELAY,0)+5);
//                            Toast.makeText(context, "Snoozed time", Toast.LENGTH_SHORT).show();
//                        }
//
//                        handler.postDelayed(this, 5000);
//                }
//            }).start();

    }

    public boolean isEmulator() {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("sdk_gphone64_arm64")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator");
    }
}
