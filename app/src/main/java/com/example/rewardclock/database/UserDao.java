package com.example.rewardclock.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.rewardclock.database.entities.Alarms;
import com.example.rewardclock.database.entities.User;

import java.util.List;

@Dao
public interface UserDao {
//    @Query("SELECT * FROM user")
//    List<User> getAll();
//
//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    List<User> loadAllByIds(int[] userIds);
//
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Query("SELECT EXISTS(SELECT * FROM User WHERE uid= :userId)")
    Boolean is_exist(int userId);

    @Query("SELECT * FROM User")
    List<User> getallusers();

    @Query("DELETE FROM User WHERE uid = :id")
    void deleteById(int id);


    @Insert
    void insertrecord(User... users);
}

@Dao
interface AlarmDao{
    @Insert
    Void insertAlarm(Alarms... alarms);

    @Query("SELECT EXISTS ( SELECT * FROM Alarms WHERE id = :id)")
    Boolean isAlarmExists(int id);

    @Query("SELECT * FROM Alarms WHERE id = :id LIMIT 1")
    Alarms getOneAlarmById(int id);

    @Query("SELECT * FROM Alarms")
    List<Alarms> getAllAlarms();

    @Query("DELETE FROM Alarms WHERE id = :id")
    void deleteAlarmById(int id);

    @Query("UPDATE Alarms SET onoff = :boolOnOff WHERE id= :id")
    void updateAlarmOnOffById(int id,boolean boolOnOff);

    @Query("SELECT * FROM Alarms WHERE id > :id AND onoff = 1 LIMIT 1")
    Alarms getUpcomingAlarm(int id);

    @Query("SELECT EXISTS( SELECT * FROM Alarms WHERE id>:currentTime and id<:snoozeTime)")
    boolean isAlarmExistsInSnoozeTime(int currentTime,int snoozeTime);

}
