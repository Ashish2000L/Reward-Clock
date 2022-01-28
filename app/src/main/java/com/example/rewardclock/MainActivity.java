package com.example.rewardclock;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.transition.CircularPropagation;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.Toast;

import com.example.rewardclock.database.RoomAlarmDatabase;
import com.example.rewardclock.utils.setAlarm;
import com.google.android.material.shape.CutCornerTreatment;
import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
//import com.tomergoldst.tooltips.ToolTip;
//import com.tomergoldst.tooltips.ToolTipAnimator;
//import com.tomergoldst.tooltips.ToolTipsManager;

import java.text.SimpleDateFormat;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static com.example.rewardclock.broadcasts.AlarmReceiver.ringtone;
import static com.example.rewardclock.splash.SHARED_PREF;

public class MainActivity extends AppCompatActivity {

    public static final String IS_FIRST_TOOLTIP_ALARM="first_time_tooltip_alarm";

    private final int LEFT_CORNERS_CUT=0,RIGHT_CORNERS_CUT=1,TOP_CORNERS_CUT=3,BOTTOM_CORNERS_CUT=4;

    // Database variables
    private int HOUR, MINUTE, DBID;
    private String MESSAGE;
    private long timestamp;
    private Boolean AM_PM,ON_OFF,IS_EVERYDAY;

    setAlarm setAlarm = new setAlarm();

    TextView hourtv,minutetv,amtv,pmtv;
    Button btnsave;
    ImageView edit_img;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    ConstraintLayout constraintLayout;
    EditText messageet;
    LinearLayout time_linear_layout;
    String TAG="alam_clock";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // castings

        hourtv = findViewById(R.id.hour);
        minutetv = findViewById(R.id.minutes);
        edit_img = findViewById(R.id.edit_image);
        constraintLayout = findViewById(R.id.main_body);
        time_linear_layout = findViewById(R.id.time_linear_layout);
        messageet = findViewById(R.id.etmsg);
        btnsave =findViewById(R.id.btn_set_alarm);
        amtv = findViewById(R.id.amtv);
        pmtv = findViewById(R.id.pmtv);

        sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);


        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        hourtv.setBackground(createCutBackground(LEFT_CORNERS_CUT,R.dimen.corner_cut,R.dimen.clock_stroke_dim,R.color.purple_500,"#36C398F8"));
        minutetv.setBackground(createCutBackground(RIGHT_CORNERS_CUT,R.dimen.corner_cut,R.dimen.clock_stroke_dim,R.color.purple_500,"#36C398F8"));

        amtv.setBackground(createCutBackground(TOP_CORNERS_CUT,R.dimen.corner_cut,R.dimen.clock_stroke_dim,R.color.select_background_ampm,"#94B3B0B0"));
        pmtv.setBackground(createCutBackground(BOTTOM_CORNERS_CUT,R.dimen.corner_cut,R.dimen.clock_stroke_dim,R.color.select_background_ampm,"#94B3B0B0"));

        if (sharedPreferences.getBoolean(IS_FIRST_TOOLTIP_ALARM,true) ){
            settooltip();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IS_FIRST_TOOLTIP_ALARM,false);
            editor.apply();
        }



    }

    public void onClickSetAlarm(View view) throws ExecutionException, InterruptedException {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY,HOUR);
        calendar.set(Calendar.MINUTE,MINUTE);
        timestamp=calendar.getTimeInMillis();
        MESSAGE = messageet.getText().toString().trim();

        if (!new RoomAlarmDatabase.CheckAlarmExistById(DBID).execute(this).get()){
            new RoomAlarmDatabase.InsertAlarm(DBID,timestamp,(int)(MINUTE/10)+""+(int)(MINUTE%10),(int)((HOUR%12)/10)+""+(int)((HOUR%12)%10),MESSAGE,true,AM_PM,false).execute(this);
            Log.d(TAG, "onClickSetAlarm: new alarm set");
        }else{
            new RoomAlarmDatabase.UpdateAlarmOnOffById(DBID,true).execute(this);
            Log.d(TAG, "onClickSetAlarm: previous alarm updated");
        }
        setAlarm.setNewAlarm(this,-1);
        Toast.makeText(this, "Alarm set for "+HOUR+":"+MINUTE, Toast.LENGTH_SHORT).show();
    }

    public void OnToggleClicked(View view) {

        Calendar calendar = Calendar.getInstance();

        MaterialTimePicker timePicker=new MaterialTimePicker.Builder()
                .setTitleText("Pick Your Alarm")
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(calendar.getTime().getHours())
                .setMinute(calendar.getTime().getMinutes())
                .build();
        timePicker.show(getSupportFragmentManager(),"MainActivity");

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "You picked time -> "+timePicker.getHour()+':'+timePicker.getMinute(), Toast.LENGTH_LONG).show();;
                HOUR=timePicker.getHour();
                MINUTE = timePicker.getMinute();

                hourtv.setText(" "+(int)((HOUR%12)/10)+" "+(int)((HOUR%12)%10)+" ");
                minutetv.setText(" "+(int)(MINUTE/10)+" "+(int)(MINUTE%10)+" ");

                if (timePicker.getHour()<12){
                    //AM time is selected
                    change_am_pm(1);
                    AM_PM=true;
                }else{
                    //PM time is selected
                    change_am_pm(2);
                    AM_PM=false;
                }
                DBID = Integer.parseInt((int)(HOUR/10)+""+(int)(HOUR%10)+""+(int)(MINUTE/10)+""+(int)(MINUTE%10));
            }
        });

        /**
         if (((ToggleButton) view).isChecked()) {
         Toast.makeText(MainActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();

         // calendar is called to get current time in hour and minute
         calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
         calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

         SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

         Log.d(TAG, "OnToggleClicked: Current Date is : "+(String)format1.format(calendar.getTime()));

         Intent intent = new Intent(this, AlarmReceiver.class);


         pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

         time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
         if (System.currentTimeMillis() > time) {

         if (Calendar.AM_PM == 0)
         time = time + (1000 * 60 * 60 * 12);
         else
         time = time + (1000 * 60 * 60 * 24);
         }



         // Alarm rings continuously until toggle button is turned off
         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 60000, pendingIntent);

         // alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent);

         //            Handler handler = new Handler();
         //            long finalTime = time;
         //            Runnable runnable = new Runnable() {
         //                @Override
         //                public void run() {
         //                    if (finalTime>calendar.getTimeInMillis()+9000){
         //                        Log.d(TAG, "run: ALARM CANCLED!");
         //                        alarmManager.cancel(pendingIntent);
         //                    }
         //                    Log.d(TAG, "run: Current value is "+calendar.getTimeInMillis());
         //                    handler.postDelayed(this,1000);
         //                }
         //            };
         //            runnable.run();


         } else {
         alarmManager.cancel(pendingIntent);
         pendingIntent.cancel();
         ringtone.stop();
         //            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,60000,100000,pendingIntent);
         //            alarmManager.cancel(pendingIntent);
         Toast.makeText(MainActivity.this, "ALARM OFF", Toast.LENGTH_SHORT).show();
         }
         **/
    }


    private MaterialShapeDrawable createCutBackground(int corners, @DimenRes int dim, @DimenRes int stroke, @ColorRes int strokeColor, String filler){

        ShapeAppearanceModel.Builder shapeAppearanceModel = ShapeAppearanceModel.builder();

        float res_dim=getResources().getDimension(dim);
        switch (corners){

            case LEFT_CORNERS_CUT:
                shapeAppearanceModel.setBottomLeftCorner(new CutCornerTreatment())
                        .setTopLeftCorner(new CutCornerTreatment())
                        .setTopLeftCornerSize(res_dim)
                        .setBottomLeftCornerSize(res_dim);

                break;

            case RIGHT_CORNERS_CUT:
                shapeAppearanceModel
                        .setTopRightCorner(new CutCornerTreatment())
                        .setBottomRightCorner(new CutCornerTreatment())
                        .setBottomRightCornerSize(res_dim)
                        .setTopRightCornerSize(res_dim);

                break;

            case TOP_CORNERS_CUT:
                shapeAppearanceModel.setTopLeftCorner(new CutCornerTreatment())
                        .setTopRightCorner(new CutCornerTreatment())
                        .setTopLeftCornerSize(res_dim)
                        .setTopRightCornerSize(res_dim);

                break;

            case BOTTOM_CORNERS_CUT:
                shapeAppearanceModel
                        .setBottomLeftCorner(new CutCornerTreatment())
                        .setBottomRightCorner(new CutCornerTreatment())
                        .setBottomLeftCornerSize(res_dim)
                        .setBottomRightCornerSize(res_dim);
                break;
        }

        MaterialShapeDrawable background= new MaterialShapeDrawable(shapeAppearanceModel.build());

            background.setStroke(getResources().getDimension(stroke),
                    getResources().getColor(strokeColor));

        background.setFillColor(ColorStateList.valueOf(Color.parseColor(filler)));

        return background;
    }

    private void change_am_pm(int input){

        //AM time is selected
        if (input==1){
            amtv.setBackground(createCutBackground(TOP_CORNERS_CUT,R.dimen.corner_cut,R.dimen.clock_stroke_dim,R.color.purple_500,"#36C398F8"));
            amtv.setTextColor(getResources().getColor(R.color.purple_500));
            amtv.setText("AM");
            pmtv.setBackground(createCutBackground(BOTTOM_CORNERS_CUT,R.dimen.corner_cut,R.dimen.clock_stroke_dim,R.color.select_background_ampm,"#94B3B0B0"));
            pmtv.setTextColor(getResources().getColor(R.color.select_background_ampm));
            pmtv.setText("PM");
        }else{
            //PM time is selected

            pmtv.setBackground(createCutBackground(BOTTOM_CORNERS_CUT,R.dimen.corner_cut,R.dimen.clock_stroke_dim,R.color.purple_500,"#36C398F8"));
            pmtv.setTextColor(getResources().getColor(R.color.purple_500));
            pmtv.setText("PM");
            amtv.setBackground(createCutBackground(TOP_CORNERS_CUT,R.dimen.corner_cut,R.dimen.clock_stroke_dim,R.color.select_background_ampm,"#94B3B0B0"));
            amtv.setTextColor(getResources().getColor(R.color.select_background_ampm));
            amtv.setText("AM");
        }

    }

    private void settooltip(){
        btnsave.setEnabled(false);
        messageet.setEnabled(false);
        time_linear_layout.setClickable(false);
        new SimpleTooltip.Builder(this)
                .anchorView(edit_img)
                .text("Set Alarm")
                .gravity(Gravity.BOTTOM)
                .animated(true)
                .dismissOnOutsideTouch(true)
                .focusable(true)
                .showArrow(true)
                .arrowColor(Color.parseColor("#80C091FA"))
                .backgroundColor(Color.parseColor("#80C091FA"))
                .textColor(R.color.purple_500)
                .transparentOverlay(false)
                .ignoreOverlay(true)
                .onDismissListener(new SimpleTooltip.OnDismissListener() {
                    @Override
                    public void onDismiss(SimpleTooltip tooltip) {
                        tooltip.dismiss();
                        new SimpleTooltip.Builder(MainActivity.this)
                                .anchorView(messageet)
                                .text("Set your Message ")
                                .gravity(Gravity.END)
                                .animated(true)
                                .dismissOnOutsideTouch(true)
                                .focusable(true)
                                .showArrow(true)
                                .arrowColor(Color.parseColor("#80C091FA"))
                                .backgroundColor(Color.parseColor("#80C091FA"))
                                .textColor(R.color.purple_500)
                                .transparentOverlay(false)
                                .ignoreOverlay(true)
                                .onDismissListener(new SimpleTooltip.OnDismissListener() {
                                    @Override
                                    public void onDismiss(SimpleTooltip tooltip) {
                                        tooltip.dismiss();
                                        messageet.setEnabled(true);
                                        btnsave.setEnabled(true);
                                        time_linear_layout.setClickable(true);
                                    }
                                }).build().show();
                    }
                })
                .build()
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,base_activity.class));
    }
}