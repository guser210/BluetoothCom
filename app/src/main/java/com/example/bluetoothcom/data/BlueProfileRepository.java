package com.example.bluetoothcom.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.google.android.material.chip.ChipDrawable;

import java.util.List;

public class BlueProfileRepository {

    private BlueProfileDatabase database;
    private BlueProfileDao profileDao;

    private BlueProfile defaultProfile;
    private LiveData<List<BlueProfile>> profiles;


    public BlueProfileRepository(Application application){
        database = BlueProfileDatabase.getInstance(application);


        profileDao = database.blueProfileDao();

        defaultProfile = profileDao.getDefaultProfile();
        profiles = profileDao.getLiveProfiles();


    }



    public LiveData<List<BlueProfile>> getProfiles(){ return profileDao.getLiveProfiles();}
    public BlueProfile getDefaultProfile(){return profileDao.getDefaultProfile();}

    public BlueProfile BlueProfileWith(String name){
        return profileDao.getProfileWidth(name);
    }

    public void deleteProfileWith(String name){
        profileDao.deleteProfileWith(name);
        //new DeleteProfileWidthAsync(profileDao).execute(name);
    }
    public BlueProfile getProfileWith(String name){
        return profileDao.getProfileWidth(name);
    }
    public int getProfileNextSort(){
        return profileDao.getMaxSort() + 1;
    }
    public void insertProfile(BlueProfile profile){
        deleteProfileWith(profile.getName());
        profileDao.insert(profile);
        //new InsertProfileAsync(profileDao).execute(profile);
    }

    public void deleteProfile(BlueProfile profile){
        new DeleteProfile_Async(profileDao).execute(profile);
    }
    private static class DeleteProfile_Async extends AsyncTask<BlueProfile, Void, Void>{
        private BlueProfileDao profileDao;

        public DeleteProfile_Async(BlueProfileDao profileDao) {
            this.profileDao = profileDao;
        }

        @Override
        protected Void doInBackground(BlueProfile... blueProfiles) {
            this.profileDao.delete(blueProfiles[0]);
            return null;
        }
    }


    public void insertProfileDirect(BlueProfile profile){
        profileDao.insert(profile);
    }
//    public void insertProfile(BlueProfile profile){
//        new InsertProfileAsync(profileDao).execute(profile);
//    }

//    private static class InsertProfileAsync extends AsyncTask<BlueProfile,Void, Void>{
//        private BlueProfileDao profileDao;
//
//        public InsertProfileAsync(BlueProfileDao profileDao) {
//            this.profileDao = profileDao;
//        }
//
//        @Override
//        protected Void doInBackground(BlueProfile... blueProfiles) {
//            profileDao.insert(blueProfiles[0]);
//            return null;
//        }
//    }

    private static class DeleteProfileWidthAsync extends AsyncTask<String, Void, Void>{
        private BlueProfileDao profileDao;
        private String name;

        public DeleteProfileWidthAsync(BlueProfileDao profileDao){
            this.profileDao =  profileDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            profileDao.deleteProfileWith(strings[0]);
            return null;
        }
    }


    private static class InsertProfileAsync extends  AsyncTask<BlueProfile,Void,Void>{
        private BlueProfileDao profileDao;

        public InsertProfileAsync(BlueProfileDao profileDao){
            this.profileDao = profileDao;
        }

        @Override
        protected Void doInBackground(BlueProfile... blueProfiles) {
            profileDao.insert(blueProfiles[0]);
            //profileDao.delete(blueProfiles[0]);
            return null;
        }
    }

}
