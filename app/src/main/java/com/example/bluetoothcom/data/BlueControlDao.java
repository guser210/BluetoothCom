package com.example.bluetoothcom.data;

import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface BlueControlDao {

    //TODO: functions.
    //    1. insert control
    //    2. delete control
    //    3. update control
    //TODO:
    //    4. delete Control by control id.
    //    5. delete controls from profile by id.
    //    6. delete controls from profile by profile name.
    //TODO:
    //    4. get control by id.
    //    5. get controls for profile by profile id.
    //    6. get controls for profile by profile name.
    //TODO:
    //    7. get control by control id.
    //    8. get controls for profile by profile id.
    //    8.1. get controls for profile by profile name Live.
    //    9. get controls for profile by profile id.
    //    9.1. get controls for profile by profile name Live.


    //TODO: 1.
    @Insert
    void insert(BlueControl control);

    //TODO: 2.
    @Delete
    void delete(BlueControl control);

    //TODO: 3.
    @Update
    void update(BlueControl control);

    //TODO: 4.
    @Query("Delete from bluecontrol where id = :id")
    void deleteControlBy(int id);

    //TODO: 5.
    @Query("delete from bluecontrol where profileId = :profileId")
    void deleteControlsFromProfileBy(int profileId);
    //TODO: 6.
    @Query("delete from bluecontrol where profileName = :profileName")
    void deleteControlsFromProfileBy(String profileName);

    //TODO: 7.
    @Query("select * from BlueControl where id = :id " )
    BlueControl getControlById(int id);

    //TODO: 8.
    @Query("select * from bluecontrol where profileId =:profileId  order by sort asc")
    List<BlueControl> getControlsForProfileBy(int profileId);

    //tODO: 8.1.
    @Query("select * from bluecontrol where profileId =:profileId  order by sort asc")
    LiveData<BlueControl> getControlsForProfileBy_LIVE(int profileId);

    //TODO: 9.
    @Query("select * from bluecontrol where profileName =:profileName order by sort asc")
    List<BlueControl> getControlsForProfileBy(String profileName);

    //tODO: 9.1.
    @Query("select * from bluecontrol where profileName =:profileName order by sort asc")
    LiveData<BlueControl> getControlsForProfileBy_LIVE(String profileName);


    @Query("SELECT * FROM BlueControl where profileId = " + BlueProfile.defaultId + " order by sort asc")
    List<BlueControl> getDefaultControls();


    @Query("SELECT * FROM bluecontrol where profileId = " + BlueProfile.defaultId + " and name = :name limit 1")
    BlueControl getDefaultControlBy(String name);

    @Query("SELECT MAX(sort) from BlueControl")
    int getMaxSort();

    @Query("SELECT MAX(id) from BlueControl where profileName = :profileName")
    int getMaxControlIdForProfileWith(String profileName);

    @Query("SELECT MAX(id) from BlueControl where profileId = :profileId")
    int getMaxControlIdForProfileWith(int profileId);

    @Query("SELECT MAX(id) from BlueControl")
    int getMaxControlId();

}
