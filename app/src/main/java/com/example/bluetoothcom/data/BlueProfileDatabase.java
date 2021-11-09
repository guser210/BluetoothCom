package com.example.bluetoothcom.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.bluetoothcom.R;

import java.util.List;

@Database(entities = {BlueProfile.class, BlueControl.class, BlueControlProperty.class}, version =  3)
public abstract class BlueProfileDatabase extends RoomDatabase {

    private static BlueProfileDatabase databaseInstance;
    public abstract BlueProfileDao blueProfileDao();
    public abstract BlueControlDao blueControlDao();
    public abstract BlueControlPropertyDao blueControlPropertyDao();


    public static synchronized BlueProfileDatabase getInstance(Context context){
        if( databaseInstance == null){
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(), BlueProfileDatabase.class,"database_blue_profileV1.1")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(callback)
                    .build();
        }

       // loadDefaultData();
        return databaseInstance;
    }



    private static RoomDatabase.Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

//            new loadDefaultProfiles(databaseInstance).execute();
//            new loadDefaultControls(databaseInstance).execute();
//            new loadDefaultControlProperties(databaseInstance).execute();
        }
    };

    private static class loadDefaultProfiles extends AsyncTask<Void, Void, Void>{
        private BlueProfileDao profileDao;

        public loadDefaultProfiles(BlueProfileDatabase database) {
            this.profileDao = database.blueProfileDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            profileDao.insert( new BlueProfile(BlueProfile.defaultId, 1,BlueProfile.defaultName,"Default profile"));

            return null;
        }
    }


    private static class loadDefaultControls extends AsyncTask<Void, Void, Void>{
        private BlueControlDao controlDao;

        public loadDefaultControls(BlueProfileDatabase database){
            this.controlDao = database.blueControlDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            controlDao.insert(new BlueControl(1,BlueProfile.defaultId,1,"Console","Console", R.drawable.thumb_console));
            controlDao.insert(new BlueControl(2,BlueProfile.defaultId,2,"BigButton","Big Button",R.drawable.thumb_big_button));
            return null;
        }
    }

    private static class loadDefaultControlProperties extends AsyncTask<Void, Void, Void>{
        private BlueControlPropertyDao propertyDao;
        private BlueControlDao bcdao;

        public
        loadDefaultControlProperties(BlueProfileDatabase database){
            this.propertyDao = database.blueControlPropertyDao();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            int controlId = 1; //Console
            int sort = 1;
            propertyDao.insert(new BlueControlProperty(0,controlId,1,"Console Title",sort++,"Title","Console","",0,"",""));
            propertyDao.insert(new BlueControlProperty(0,controlId,1,"Font Size",sort++,"Text Size","18","",0,"",""));
            propertyDao.insert(new BlueControlProperty(0,controlId,1,"Max Lines",sort++,"Max Lines","100","",0,"",""));


            controlId = 2; //BigButton
            sort = 1;
            propertyDao.insert(new BlueControlProperty(0,controlId,1,"Button Title",sort++,"Title","Button","",0,"",""));
            propertyDao.insert(new BlueControlProperty(0,controlId,3,"Button Command",sort++,"Command","Button1",":",1,"Value","clicked"));



            return null;
        }
    }

}
