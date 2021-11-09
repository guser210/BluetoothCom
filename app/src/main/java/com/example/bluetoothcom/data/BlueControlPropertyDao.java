package com.example.bluetoothcom.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface BlueControlPropertyDao {
    @Insert
    void insert(BlueControlProperty property);

    @Delete
    void delete(BlueControlProperty property);

    @Update
    void update(BlueControlProperty property);

    @Query("SELECT * FROM BlueControlProperty WHERE controlid = :controlId")
    LiveData<List<BlueControlProperty>> deletePropertiesForControlBy_LIVE(int controlId);

    @Query("delete from BlueControlProperty where controlId = :controlId")
    void deletePropertiesFor(int controlId);

    @Query("SELECT * FROM BlueControlProperty where controlID = :id order by sort asc")
    List<BlueControlProperty> getPropertiesForControlWith(int id);


}
