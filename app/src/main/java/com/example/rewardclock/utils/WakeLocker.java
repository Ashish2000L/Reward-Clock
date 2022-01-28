package com.example.rewardclock.utils;

import android.content.Context;
import android.os.PowerManager;

public class WakeLocker {

    private static PowerManager.WakeLock wakeLock;

    public static void acquire(Context ctx) {
        if (wakeLock != null) wakeLock.release();

        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK|
                PowerManager.ON_AFTER_RELEASE, "wakeLocker: This is nice");
        wakeLock.acquire(2*60*1000L /*2 minutes*/);
    }

    public static void release() {
        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }

}
