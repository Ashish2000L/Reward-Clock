package com.example.rewardclock;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.rewardclock.adaptors.alarmAdaptor;
import com.example.rewardclock.database.RoomAlarmDatabase;
import com.example.rewardclock.database.entities.Alarms;

import java.util.List;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;

public class alarm_list_fragment extends Fragment {

    private Context context;

    public alarm_list_fragment() {

    }

    RecyclerView recyclerView;
    RelativeLayout layout;
    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_alarm_list_fragment, container, false);

        context=view.getContext();

        recyclerView = view.findViewById(R.id.alarm_recycle);
        button = view.findViewById(R.id.add_new_alarm_frag);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MainActivity.class));
            }
        });


        try {
            List<Alarms> alarmsList=new RoomAlarmDatabase.GetAllAlarms().execute(context).get();

            if (alarmsList.size()>0){
                button.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                alarmAdaptor alarmAdaptor = new alarmAdaptor(alarmsList,recyclerView,button);

                recyclerView.setAdapter(alarmAdaptor);

            }else{
                button.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return view;
    }
}