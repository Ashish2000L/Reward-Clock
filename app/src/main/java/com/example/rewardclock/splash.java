package com.example.rewardclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class splash extends AppCompatActivity {

    public static final String SHARED_PREF="sharedpreference";

    LottieAnimationView lottieAnimationView,logo_lottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        lottieAnimationView = findViewById(R.id.loadin_lottie);
        logo_lottie = findViewById(R.id.anim_logo_splash);

        logo_lottie.enableMergePathsForKitKatAndAbove(true);
        lottieAnimationView.enableMergePathsForKitKatAndAbove(true);

        lottieAnimationView.setAnimation(R.raw.loading);
        logo_lottie.setAnimation(R.raw.timeman_splash);
        logo_lottie.setSpeed(0.5f);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    Thread.sleep(5000);

                    Intent intent = new Intent(getApplicationContext(),base_activity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

//        Toast.makeText(this, "We started new Activity, yo bro!", Toast.LENGTH_LONG).show();
    }

}