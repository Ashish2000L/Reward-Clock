package com.example.rewardclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class base_activity extends AppCompatActivity {

    private final int ID_HOME=1;
    private final int ID_REWARD=2;

    MeowBottomNavigation meowBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_activity);

        meowBottomNavigation = findViewById(R.id.bottom_nav);

        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME,R.drawable.home));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_REWARD,R.drawable.coin));

        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                Toast.makeText(getApplicationContext(),"You clicked Item : "+item.getId(),Toast.LENGTH_LONG).show();
            }
        });

        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                String name="";

                switch (item.getId())
                {
                    case ID_HOME: name="HOME";
                        break;
                    case ID_REWARD: name="REWARDS";
                        break;
                }

                Log.d("nav_bar", "invoke: Current screen is "+name);
            }
        });

        meowBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                String name="";

                switch (item.getId())
                {
                    case ID_HOME: name="HOME";
                        break;
                    case ID_REWARD: name="REWARDS";
                        break;
                }

                Log.d("nav_bar", "invoke: reselect screen is "+name);
            }
        });



        meowBottomNavigation.show(ID_HOME,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.alarm_frame_layout,new alarm_list_fragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_frag_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.add_alarm){
            startActivity(new Intent(this,MainActivity.class));

        }

        return true;
    }
}