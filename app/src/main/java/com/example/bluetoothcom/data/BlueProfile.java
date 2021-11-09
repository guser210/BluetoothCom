package com.example.bluetoothcom.data;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.bluetoothcom.R;

import java.nio.Buffer;
import java.nio.ByteBuffer;

@Entity
public class BlueProfile {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public final static String defaultName = "__Default__";
    public final static int defaultId = -1;

    private int sort;

    private String name;
    private String description;
    private String deviceName;
    private String deviceId;
    private String serviceId;
    private String characteristicID;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private ByteBuffer profileImage;

    private int imageWidth;
    private int imageHeight;

    public BlueProfile(int id , int sort, String name, String description) {
        this.id = id;
        this.sort = sort;
        this.name = "";
        this.description = "";

        if( name  != null)
            this.name = name;
        if( description != null)
            this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setCharacteristicID(String characteristicID) {
        this.characteristicID = characteristicID;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public void setProfileImage(ByteBuffer profileImage) {
        this.profileImage = profileImage;
    }

    public ByteBuffer getProfileImage() {
        return profileImage;
    }

    public int getId() {
        return id;
    }

    public static String getDefaultName() {
        return defaultName;
    }

    public int getSort() {
        return sort;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getCharacteristicID() {
        return characteristicID;
    }

    public Bitmap getProfileImageBitmap(){

        if( profileImage == null ) return null;
        profileImage.rewind();
        Bitmap bmp = Bitmap.createBitmap(imageWidth,
                imageHeight,
                Bitmap.Config.ARGB_8888);

        bmp.copyPixelsFromBuffer(profileImage);

        return bmp;

    }


}
