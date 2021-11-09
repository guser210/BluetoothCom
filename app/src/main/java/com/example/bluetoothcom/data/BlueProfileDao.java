package com.example.bluetoothcom.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BlueProfileDao {

    @Query("SELECT MAX(sort) from BlueProfile")
    int getMaxSort();
    
    @Insert
    void insert(BlueProfile profile);

    @Delete
    void delete( BlueProfile profile);

    @Query("DELETE FROM BlueProfile where name = :name")
    void deleteProfileWith(String name);

    @Query("SELECT * FROM BlueProfile where id = " + BlueProfile.defaultId )
    BlueProfile getDefaultProfile();

    @Query("SELECT * FROM BlueProfile where name = :name" )
    BlueProfile getProfileWidth(String name);


    @Query("SELECT * FROM BlueProfile where id =:id " )
    BlueProfile getProfileBy(int id);


    @Query("SELECT * FROM BlueProfile where name = '" + BlueProfile.defaultName + "'")
    LiveData<List<BlueProfile>> getLiveDefaultProfiles();

    @Query("SELECT * FROM BlueProfile where name <> '" + BlueProfile.defaultName + "'")
    LiveData<List<BlueProfile>> getLiveProfiles();

    @Query("SELECT * FROM BlueProfile where name = :name")
    LiveData<List<BlueProfile>> getLiveProfileBy(String name);

    @Query("DELETE from blueprofile")
    void deleteAllProfiles();
    @Query("DELETE from bluecontrol")
    void deleteAllControls();
    @Query("DELETE from bluecontrolproperty")
    void deleteAllControlProperties();

    @Query("DELETE FROM BlueProfile where name = '" + BlueProfile.defaultName + "'")
    void deleteDefaultProfile();

    @Query("delete from BlueControl where id < 0")
    void deleteDefaultControls();

    @Query("delete from BlueControlProperty where controlID < 0")
    void deleteDefaultControlProperties();
}
