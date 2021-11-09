package com.example.bluetoothcom.data;

import android.app.Application;

import com.example.bluetoothcom.R;

public class DataRepository {
    private BlueProfileDatabase database;
    private BlueProfileDao profileDao;
    private BlueControlDao controlDao;
    private BlueControlPropertyDao propertyDao;


    public DataRepository(Application application){
        database = BlueProfileDatabase.getInstance(application);


        profileDao = database.blueProfileDao();

        controlDao = database.blueControlDao();
        propertyDao = database.blueControlPropertyDao();

    }

    public void deleteAllProfiles(){
        profileDao.deleteAllProfiles();
    }
    public void deleteAllControls(){
        profileDao.deleteAllControls();
    }
    public void deleteAllProperties(){
        profileDao.deleteAllControlProperties();
    }
    public void clearDatabase(){
        deleteAllProperties();
        deleteAllControls();
        deleteAllProfiles();
    }

    public void clearDefaultData(){
        profileDao.deleteDefaultControlProperties();
        profileDao.deleteDefaultControls();
        profileDao.deleteDefaultProfile();
    }
    public void loadDefaultData(){

        //clearDatabase();

        //TODO: load default profile.
        // this profile is the base for user controls.
        int sort = 1;
        profileDao.insert( new BlueProfile(BlueProfile.defaultId, sort,BlueProfile.defaultName,"Default profile"));

        //TODO: default user controls
        sort = 1;
        controlDao.insert(new BlueControl(-1,BlueProfile.defaultId,sort++,"Console","Console", R.drawable.thumb_console,800,800));

        controlDao.insert(new BlueControl(-2,BlueProfile.defaultId,sort++,"BigButton","Big Button",R.drawable.thumb_big_button,-2,-2));

        controlDao.insert(new BlueControl(-3,BlueProfile.defaultId,sort++,"StatusLabel","Status Label",R.drawable.thumb_status_label,-2,-2));

        controlDao.insert(new BlueControl(-4,BlueProfile.defaultId,sort++,"Odometer","Odometer",R.drawable.thumb_odometer,800,800));



        //TODO: default user control properties.
        int controlId = -1; //Console
        sort = 1;
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"ConsoleTitle",sort++,"Title","Console","",0,"",""));
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"TextSize",sort++,"Text Size","24","",0,"",""));
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"MaxLines",sort++,"Max Lines","100","",0,"",""));
        propertyDao.insert(new BlueControlProperty(0,controlId,4,"EchoControls",sort++,"Echo Controls","1","",0,"",""));



        controlId = -2; //BigButton
        sort = 1;
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"ButtonTitle",sort++,"Title","Button","",0,"",""));
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"TextSize",sort++,"Text Size","36",":",1,"Value","clicked"));
        propertyDao.insert(new BlueControlProperty(0,controlId,3,"ButtonCommand",sort++,"Command","Button1",":",1,"Value","clicked"));

        controlId = -3; //StatusLabel
        sort = 1;
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"Label",sort++,"Label","Status","",0,"",""));
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"TextSize",sort++,"Label Text Size","24",":",1,"Value","clicked"));
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"ValueTextSize",sort++,"Value Text Size","24",":",1,"Value","clicked"));
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"Width",sort++,"Width","500",":",1,"Value","clicked"));
        propertyDao.insert(new BlueControlProperty(0,controlId,3,"Value",sort++,"Value","Value",":",1,"ValueLabel","NA"));

        controlId = -4; //Odometer
        sort = 1;
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"ScaleX",sort++,"Scale X","100",":",1,"","clicked"));
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"ScaleY",sort++,"Scale Y","100",":",1,"",""));
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"TextColor",sort++,"Text Color","ffffffff","",1,"",""));
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"GearTextColor",sort++,"Gear Text Color","ff00ff00","",1,"",""));
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"Background",sort++,"Background Color","ff000000","",1,"",""));
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"ScaleColor",sort++,"Scale Color","ffffffff","",1,"",""));
        propertyDao.insert(new BlueControlProperty(0,controlId,1,"RingColor",sort++,"Ring Color","ffff0000","",1,"",""));

        propertyDao.insert(new BlueControlProperty(0,controlId,3,"RPM",sort++,"RPM","RPM",":",1,"Value","4000"));
        propertyDao.insert(new BlueControlProperty(0,controlId,3,"Gear",sort++,"Gear","GEAR",":",1,"Value","N"));

    }

}
