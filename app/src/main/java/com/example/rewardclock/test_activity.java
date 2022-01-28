package com.example.rewardclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rewardclock.adaptors.myadaptor;
import com.example.rewardclock.database.AppDatabase;
import com.example.rewardclock.database.UserDao;
import com.example.rewardclock.database.entities.User;

import java.util.List;

public class test_activity extends AppCompatActivity {

    EditText t1,t2,t3;
    TextView data;
    Button b1;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activity);

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);

        recyclerView=findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        b1 = findViewById(R.id.b1);

        getroomdata();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            new bgthead(++val).start();
                AppDatabase.AppDatabases db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.AppDatabases.class, "room_db").allowMainThreadQueries().build();
                UserDao userDao = db.userDao();
                boolean check = userDao.is_exist(Integer.parseInt(t3.getText().toString().trim()));
                if (!check) {
                    userDao.insertrecord(new User(Integer.parseInt(t3.getText().toString().trim()), t1.getText().toString().trim(), t2.getText().toString().trim()));
                    Toast.makeText(test_activity.this, "Inserted successfully!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(test_activity.this, "Record Already exists!!", Toast.LENGTH_LONG).show();
                }
                t1.setText("");
                t2.setText("");
                t3.setText("");

                getroomdata();

//                List<User> users = userDao.getallusers();
//                String str="";
//
//                for(User user : users){
//                    str=str+"\t "+user.getUid()+" "+user.getFirstName()+" "+user.getLastName()+"\n\n";
//                }
//
//                data.setText(str);
            }
        });

    }

    public static boolean isTimeAutomatic(Context c) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
//        } else {
        return android.provider.Settings.System.getInt(c.getContentResolver(), Settings.System.AUTO_TIME, 0) == 1;

    }

    public void getroomdata(){
        AppDatabase.AppDatabases db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.AppDatabases.class, "room_db").allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
        List<User> users = userDao.getallusers();
        myadaptor adaptor = new myadaptor(users);
        Log.d("sample", "getroomdata: "+adaptor.getItemCount());
        recyclerView.setAdapter(adaptor);
        adaptor.notifyDataSetChanged();

    }

    class bgthead extends Thread{

        int params;
        public bgthead(int params) {
            this.params = params;
        }

        public void run(){
            super.run();

            AppDatabase.AppDatabases db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.AppDatabases.class, "room_db").allowMainThreadQueries().build();
            UserDao userDao = db.userDao();
            boolean check = userDao.is_exist(Integer.parseInt(t3.getText().toString().trim()));
            userDao.insertrecord(new User(params++,t1.getText().toString().trim(),t2.getText().toString().trim()));

//            t1.setText("");
//            t2.setText("");
//
//            Toast.makeText(test_activity.this, "Inserted successfully!", Toast.LENGTH_LONG).show();
        }
    }
}