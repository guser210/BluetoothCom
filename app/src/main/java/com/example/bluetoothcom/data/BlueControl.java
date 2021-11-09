package com.example.bluetoothcom.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class BlueControl {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int profileId;

    private int sort;
    private String name;
    private String displayName;
    private int imageId;


    public BlueControl(int id,
                       int profileId,
                       int sort,
                       String name,
                       String displayName,
                       int imageId
    ) {
        this.id = id;
        this.sort = sort;
        this.name = name;

        this.displayName = displayName;
        this.imageId = imageId;
        this.profileId = profileId;
    }

    @Ignore
    public BlueControl(int id,
                       int profileId,
                       int sort,
                       String name,
                       String displayName,
                       int imageId,
                       int width,
                       int height
    ) {
        this.id = id;
        this.sort = sort;
        this.name = name;

        this.displayName = displayName;
        this.imageId = imageId;
        this.profileId = profileId;
        this.width = width;
        this.height = height;
    }

    private String imageName;
    private String profileName;

    private float x = 0;
    private float y = 0;
    private float width;
    private float height;

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public int getProfileId() {
        return profileId;
    }

    public int getSort() {
        return sort;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImageName() {
        return imageName;
    }

    public String getProfileName() {
        return profileName;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
