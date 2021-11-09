package com.example.bluetoothcom.data;


import android.app.Application;
import android.app.AsyncNotedAppOp;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class BlueControlRepository {

    private BlueControlDao controlDao;
    private LiveData<List<BlueControl>> allControls;
    private List<BlueControl> defaultControls;

    public BlueControlRepository(Application application){
        BlueProfileDatabase db = BlueProfileDatabase.getInstance(application.getApplicationContext());
        controlDao = db.blueControlDao();
        defaultControls = controlDao.getDefaultControls();
    }
    //TODO:
    //    1. insert control
    //    2. delete control
    //    3. update control
    //TODO:
    //    4. delete Control by control id.
    //    5. delete controls from profile by id.
    //    6. delete controls from profile by profile name.
    //TODO:
    //    7. get control by id.
    //    8. get controls for profile by profile id.
    //      8.1 LIVE get controls for profile by profile id.
    //    9. get controls for profile by profile name.
    //      9.1 LIVE get controls for profile by profile name.
    //
    //TODO: Default contorls
    //    10. get default controls by control name
    //    11. get default controls



    //TODO 1. insert control
    public void insertControl(BlueControl control){
        controlDao.insert(control);
//        new insertControlAsync(controlDao).execute(control);
    }
    //TODO 1 class.
    private static class insertControlAsync extends AsyncTask<BlueControl,Void, Void>{
        private BlueControlDao controlDao;

        public insertControlAsync(BlueControlDao controlDao) {  this.controlDao = controlDao;  }

        @Override
        protected Void doInBackground(BlueControl... blueControls) {
            controlDao.insert(blueControls[0]);
            return null;
        }
    }


    //TODO 2. delete control
    public void deleteControl(BlueControl control){
        new deleteControlAsync(controlDao).execute(control);
    }
    //TODO 2 class.
    private static class deleteControlAsync extends AsyncTask<BlueControl,Void, Void>{
        private BlueControlDao controlDao;

        public deleteControlAsync(BlueControlDao controlDao) {  this.controlDao = controlDao;  }

        @Override
        protected Void doInBackground(BlueControl... blueControls) {
            controlDao.delete(blueControls[0]);
            return null;
        }
    }

    //TODO 3. update control
    public void updateControl(BlueControl control){
        new updateControl_Async(controlDao).execute(control);
    }
    //TODO 3 class.
    private static class updateControl_Async extends AsyncTask<BlueControl,Void, Void>{
        private BlueControlDao controlDao;

        public updateControl_Async(BlueControlDao controlDao) {  this.controlDao = controlDao;  }

        @Override
        protected Void doInBackground(BlueControl... blueControls) {
            controlDao.update(blueControls[0]);
            return null;
        }
    }

    //TODO: 4. delete Control by control id.
    public void deleteControlBy( int id){
        new deleteControlById_Async(controlDao).execute(id);
    }
    //TODO: 4 class.
    private static class deleteControlById_Async extends AsyncTask<Integer,Void,Void>{
        private BlueControlDao controlDao;

        public deleteControlById_Async(BlueControlDao controlDao) {
            this.controlDao = controlDao;
        }

        @Override
        protected Void doInBackground(Integer... controlIds) {
            this.controlDao.deleteControlBy(controlIds[0]);
            return null;
        }
    }

    //TODO: 5. delete controls from profile by id.
    public void deleteControlsFromProfileBy(int profileId){
        new deleteControlsByProfileId_Async(controlDao).execute(profileId);
    }
    //TODO: 5 class.
    private static class deleteControlsByProfileId_Async extends AsyncTask<Integer, Void, Void>{
        private BlueControlDao controlDao;

        public deleteControlsByProfileId_Async(BlueControlDao controlDao) { this.controlDao = controlDao; }

        @Override
        protected Void doInBackground(Integer... profileIds) {
            this.controlDao.deleteControlsFromProfileBy(profileIds[0]);
            return null;
        }
    }

    //TODO: 6. delete controls from profile by profile name.
    public void deleteControlsFromProfileBy(String profileName){
        new deleteControlsByProfileName_Async(controlDao).execute(profileName);
    }
    //TODO: 6. class.
    private static class deleteControlsByProfileName_Async extends AsyncTask<String, Void, Void>{
        private BlueControlDao controlDao;

        public deleteControlsByProfileName_Async(BlueControlDao controlDao) {this.controlDao = controlDao;}

        @Override
        protected Void doInBackground(String... profileName) {
            this.controlDao.deleteControlsFromProfileBy(profileName[0]);
            return null;
        }
    }
    //TODO: 7. get control by id.
    public BlueControl getControlBy(int id){
        return this.controlDao.getControlById(id);
    }

    //TODO:    8. get controls for profile by profile id.
    public ArrayList<BlueControl> getControlsFromProfileBy(int profileId){
        return (ArrayList<BlueControl>)this.controlDao.getControlsForProfileBy(profileId);
    }
    //TODO:      8.1 LIVE get controls for profile by profile id.
    public LiveData<BlueControl> getControlsFromProfileBy_LIVE(int profileId){
        return this.controlDao.getControlsForProfileBy_LIVE(profileId);
    }

    //TODO:    9. get controls for profile by profile name.
    public ArrayList<BlueControl> getControlsFromProfileBy(String profileName){
        return (ArrayList<BlueControl>)this.controlDao.getControlsForProfileBy(profileName);
    }
    //TODO:      9.1 LIVE get controls for profile by profile name.
    public LiveData<BlueControl> getControlsFromProfileBy_LIVE(String profileName){
        return this.controlDao.getControlsForProfileBy_LIVE(profileName);
    }


    //TODO: 10. get default controls by control name
    public BlueControl getDefaultControlBy(String controlName){
        return controlDao.getDefaultControlBy(controlName);
    }

    //TODO: 11. get default controls
    public List<BlueControl> getDefaultControls() {
        return controlDao.getDefaultControls();
    }

    public int getMaxControlIdForProfileWith(String profileName){
        return controlDao.getMaxControlIdForProfileWith(profileName);
    }
    public int getMaxControlIdForProfileWith(int profileId){
        return controlDao.getMaxControlIdForProfileWith(profileId);
    }
    public int getMaxControlId(){
        return controlDao.getMaxControlId();
    }
    public int getMaxSort(){
        return controlDao.getMaxSort();
    }
//
//
//
//    public ArrayList<BlueControl> getControlsForProfileBy(String profileName){
//        //return controlDao.getControlsFor(profileName);
//        return (ArrayList<BlueControl>) controlDao.getControlsForProfileBy(profileName);
//    }
//    public ArrayList<BlueControl> getControlsForProfileBy(int profileId){
//        //return controlDao.getControlsFor(profileName);
//        return (ArrayList<BlueControl>)controlDao.getControlsForProfileBy(profileId);
//    }
//
//
//
//
//    public LiveData<BlueControl>  getLiveControlsForProfileBy(int profileId){
//        return controlDao.getLiveControlsForProfileBy(profileId);
//    }
//
//    public LiveData<BlueControl>  getLiveControlsForProfileBy(String profileName){
//        return controlDao.getLiveControlsForProfileBy(profileName);
//    }
//
//
//
//    public void deleteControlsFor(int profileId){
//        controlDao.deleteControlsForProfileBy(profileId);
//    }
//    public void deleteControlFrom(String controlName, String profileName){
//        String[] parms = {profileName,controlName};
//
//        new DeleteControlFromAsync(controlDao).execute(parms);
//    }
//
//    public void insertControl(BlueControl control){
//        new BlueControlRepository.InsertControlAsync(controlDao).execute(control);
//
//    }
//
//    private static class DeleteControlFromAsync extends AsyncTask<String, Void, Void> {
//        private BlueControlDao controlDao;
//        private String name;
//
//        public DeleteControlFromAsync(BlueControlDao controlDao){
//            this.controlDao =  controlDao;
//        }
//
//        @Override
//        protected Void doInBackground(String... strings) {
//            controlDao.deleteControlFromProfile(strings[0],strings[1]);
//            return null;
//        }
//    }
//
//
//    private static class InsertControlAsync extends  AsyncTask<BlueControl,Void,Void>{
//        private BlueControlDao controlDao;
//
//        public InsertControlAsync(BlueControlDao controlDao){
//            this.controlDao = controlDao;
//        }
//
//        @Override
//        protected Void doInBackground(BlueControl... blueControls) {
//            this.controlDao.insert(blueControls[0]);
//            return null;
//        }
//
//    }



}
