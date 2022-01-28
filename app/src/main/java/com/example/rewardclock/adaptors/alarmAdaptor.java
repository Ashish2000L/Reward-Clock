package com.example.rewardclock.adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rewardclock.MainActivity;
import com.example.rewardclock.R;
import com.example.rewardclock.utils.*;
import com.example.rewardclock.database.RoomAlarmDatabase;
import com.example.rewardclock.database.entities.Alarms;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class alarmAdaptor extends RecyclerView.Adapter<alarmAdaptor.alarmViewHolder> {

    List<Alarms> alarmsList;
    public static final String ALARMID="ALARM_ID";
    setAlarm setAlarm = new setAlarm();
    RecyclerView recyclerView;
    Button layout;

    public alarmAdaptor(List<Alarms> alarmsList, @Nullable RecyclerView recyclerView, @Nullable Button relativeLayout) {
        this.alarmsList = alarmsList;
        this.layout = relativeLayout;
        this.recyclerView =recyclerView;
    }

    @NonNull
    @Override
    public alarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_row_design,parent,false);
        return new alarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull alarmViewHolder holder, int position) {

        Context context=holder.imgMorningEvening.getContext();

        holder.tvTime.setText(alarmsList.get(position).getHour()+":"+alarmsList.get(position).getMinute());

        holder.tvAmPm.setText(alarmsList.get(position).getAmpm() ? "AM" : "PM");

        holder.switchOnOff.setChecked(alarmsList.get(position).getOnoff());

        holder.imgMorningEvening.setImageResource((alarmsList.get(position).getId()>600 && alarmsList.get(position).getId()<1800)? R.drawable.glowing_sun : R.drawable.moon_stars);

        // Edit on click listener
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MainActivity.class).putExtra(ALARMID,alarmsList.get(position).getId()));
            }
        });

        // Delete on click listener
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RoomAlarmDatabase.DeleteAlarmById(alarmsList.get(position).getId()).
                        execute(v.getContext());

                alarmsList.remove(position);

                assert recyclerView != null;
                if (getItemCount() > 0) {
                    layout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                } else {
                    layout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                setAnimations(holder.itemView);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());

                try {
                    setAlarm.setNewAlarm(v.getContext(),-1);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        //switch on click listener
        holder.switchOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RoomAlarmDatabase.UpdateAlarmOnOffById(alarmsList.get(position).getId(),!alarmsList.get(position).getOnoff()).execute(v.getContext());
                alarmsList.get(position).setOnoff(!alarmsList.get(position).getOnoff());
                notifyDataSetChanged();

                try {
                    setAlarm.setNewAlarm(v.getContext(),-1);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmsList.size();
    }

    static class alarmViewHolder extends RecyclerView.ViewHolder{

        TextView tvTime,tvAmPm;
        Switch switchOnOff;
        ImageButton imgEdit,imgDelete;
        ImageView imgMorningEvening;

        public alarmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.time_text);
            tvAmPm = itemView.findViewById(R.id.am_pm_text);
            switchOnOff = itemView.findViewById(R.id.off_on_switch);
            imgEdit = itemView.findViewById(R.id.edit_img_btn);
            imgDelete = itemView.findViewById(R.id.delete_alarm);
            imgMorningEvening = itemView.findViewById(R.id.day_img);
        }
    }


    public void setAnimations(View viewToAnimate){

        Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.right_slide_out_anim);
        animation.setDuration(1000);
        animation.setInterpolator(new DecelerateInterpolator());
        viewToAnimate.startAnimation(animation);

    }
}
