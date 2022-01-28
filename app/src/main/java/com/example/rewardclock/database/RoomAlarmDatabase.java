package com.example.rewardclock.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.rewardclock.adaptors.alarmAdaptor;
import com.example.rewardclock.database.entities.Alarms;

import java.util.DoubleSummaryStatistics;
import java.util.List;

public class RoomAlarmDatabase {

    public static final String TAG = "db_values", ALARMDATABASE = "alarmdb";

    public static class InsertAlarm extends AsyncTask<Context, Void, Boolean> {

        int id;
        long timestamp;
        Boolean onoff, ampm, is_everyday;
        String minute, hour, message;

        public InsertAlarm(int id, long timestamp, String minute, String hour, String message, Boolean onoff, Boolean ampm, Boolean is_everyday) {
            this.id = id;
            this.timestamp = timestamp;
            this.minute = minute;
            this.hour = hour;
            this.message = message;
            this.ampm = ampm;
            this.onoff = onoff;
            this.is_everyday = is_everyday;
        }

        @Override
        protected Boolean doInBackground(Context... contexts) {

//            try {
                AlarmDatabase db = Room.databaseBuilder(contexts[0],
                        AlarmDatabase.class, RoomAlarmDatabase.ALARMDATABASE).build();
                AlarmDao alarmDao = db.alarmDao();

                alarmDao.insertAlarm(new Alarms(id, timestamp, hour, minute, message, ampm, onoff, is_everyday));
            Log.d(TAG, "doInBackground: Data inserted successfully -> "+id+" "+timestamp+" "+hour+" "+minute+" "+ampm+ " "+message+" "+onoff+" "+is_everyday);
//            } catch (Exception e) {
//                Log.e(RoomAlarmDatabase.TAG, ""+e);
//                return false;
//            }
            return true;
        }
    }

    public static class GetAllAlarms extends AsyncTask<Context, Void, List<Alarms>> {

        public static List<Alarms> alarmsList;
//        alarmAdaptor alarmAdaptor;
//        RecyclerView recyclerView;
//        boolean setToList=false;

//        public GetAllAlarms(@Nullable RecyclerView recyclerView, boolean setToList) {
//            this.recyclerView=recyclerView;
//            this.setToList = setToList;
//        }

        @Override
        protected List<Alarms> doInBackground(Context... contexts) {

            AlarmDatabase db = Room.databaseBuilder(contexts[0],
                    AlarmDatabase.class, RoomAlarmDatabase.ALARMDATABASE).build();
            AlarmDao alarmDao = db.alarmDao();

            alarmsList = alarmDao.getAllAlarms();

            return alarmsList;
        }

//        @Override
//        protected void onPostExecute(List<Alarms> alarms) {
//            super.onPostExecute(alarms);
//
//            if (setToList) {
//                alarmAdaptor = new alarmAdaptor(alarms);
//                recyclerView.setAdapter(alarmAdaptor);
//            }
//        }
    }

    public static class UpdateAlarmOnOffById extends AsyncTask<Context, Void, Void> {

        int id;
        public boolean boolOnOffStatus=true;

        public UpdateAlarmOnOffById(int id,boolean boolOnOffStatus) {
            this.id = id;
            this.boolOnOffStatus = boolOnOffStatus;
        }

        @Override
        protected Void doInBackground(Context... contexts) {

            AlarmDatabase db = Room.databaseBuilder(contexts[0],
                    AlarmDatabase.class, RoomAlarmDatabase.ALARMDATABASE).build();
            AlarmDao alarmDao = db.alarmDao();

            alarmDao.updateAlarmOnOffById(id,boolOnOffStatus);

            return null;
        }

    }


    public static class CheckAlarmExistById extends AsyncTask<Context, Void, Boolean> {

        int id;
        public boolean ISEXISTS=true;

        public CheckAlarmExistById(int id) {
            this.id = id;
        }

        @Override
        protected Boolean doInBackground(Context... contexts) {

            AlarmDatabase db = Room.databaseBuilder(contexts[0],
                    AlarmDatabase.class, RoomAlarmDatabase.ALARMDATABASE).build();
            AlarmDao alarmDao = db.alarmDao();

            return alarmDao.isAlarmExists(id);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            ISEXISTS=aBoolean;
        }
    }

    public static class DeleteAlarmById extends AsyncTask<Context,Void,Void>{

        int id;

        public DeleteAlarmById(int id) {
            this.id = id;
        }

        @Override
        protected Void doInBackground(Context... contexts) {

            AlarmDatabase db = Room.databaseBuilder(contexts[0],
                    AlarmDatabase.class, RoomAlarmDatabase.ALARMDATABASE).build();
            AlarmDao alarmDao = db.alarmDao();

            alarmDao.deleteAlarmById(id);

            return null;
        }
    }

    public static class SelectOneAlarmById extends AsyncTask<Context,Void,Alarms>{

        int id;

        public SelectOneAlarmById(int id) {
            this.id = id;
        }

        @Override
        protected Alarms doInBackground(Context... contexts) {
            AlarmDatabase db = Room.databaseBuilder(contexts[0],
                    AlarmDatabase.class, RoomAlarmDatabase.ALARMDATABASE).build();
            AlarmDao alarmDao = db.alarmDao();

            return alarmDao.getOneAlarmById(id);
        }
    }

    public static class GetUpcomingAlarms extends AsyncTask<Context,Void,Alarms>{

        int id;

        public GetUpcomingAlarms(int id) {
            this.id = id;
        }

        @Override
        protected Alarms doInBackground(Context... contexts) {

            AlarmDatabase db = Room.databaseBuilder(contexts[0],
                    AlarmDatabase.class, RoomAlarmDatabase.ALARMDATABASE).build();
            AlarmDao alarmDao = db.alarmDao();

            return alarmDao.getUpcomingAlarm(id);
        }
    }

    public static class IsAlarmExistsInSnoozeTime extends AsyncTask<Context,Void,Boolean>{

        int currentTime,snoozeTime;

        public IsAlarmExistsInSnoozeTime(int currentTime, int snoozeTime) {
            this.currentTime = currentTime;
            this.snoozeTime = snoozeTime;
        }

        @Override
        protected Boolean doInBackground(Context... contexts) {

            AlarmDatabase db = Room.databaseBuilder(contexts[0],
                    AlarmDatabase.class, RoomAlarmDatabase.ALARMDATABASE).build();
            AlarmDao alarmDao = db.alarmDao();

            return alarmDao.isAlarmExistsInSnoozeTime(currentTime,snoozeTime);
        }
    }
}
