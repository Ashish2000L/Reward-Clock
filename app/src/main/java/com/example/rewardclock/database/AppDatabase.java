package com.example.rewardclock.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.rewardclock.database.entities.Alarms;
import com.example.rewardclock.database.entities.User;


public class AppDatabase{

    @Database(entities = {User.class}, version = 1)
    public static abstract class AppDatabases extends RoomDatabase {
        public abstract UserDao userDao();
    }
}

@Database(entities = {Alarms.class}, version = 1)
abstract class AlarmDatabase extends RoomDatabase {
    public abstract AlarmDao alarmDao();
}


