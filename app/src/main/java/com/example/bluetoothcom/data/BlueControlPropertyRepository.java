package com.example.bluetoothcom.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class BlueControlPropertyRepository {

    private BlueControlPropertyDao propertyDao;

    private LiveData<List<BlueControlProperty>> controlProperties;


    //TODO: insert property Async
    public void insertProperty(BlueControlProperty property){
        propertyDao.insert(property);
        //new insertProperty_Async(propertyDao).execute(property);
    }
    //TODO: CLASS insert property Async
    private static class insertProperty_Async extends AsyncTask<BlueControlProperty, Void, Void>{
        private BlueControlPropertyDao propertyDao;

        public insertProperty_Async(BlueControlPropertyDao propertyDao) {
            this.propertyDao = propertyDao;
        }

        @Override
        protected Void doInBackground(BlueControlProperty... property) {
            this.propertyDao.insert(property[0]);
            return null;
        }
    }

    //TODO: delete property.
    public void deleteProperty(BlueControlProperty controlProperty){
        new deleteProperty_Asyc(propertyDao).execute(controlProperty);
    }
    private static class deleteProperty_Asyc extends AsyncTask<BlueControlProperty, Void, Void>{
        private  BlueControlPropertyDao propertyDao;

        public deleteProperty_Asyc(BlueControlPropertyDao propertyDao) {
            this.propertyDao = propertyDao;
        }

        @Override
        protected Void doInBackground(BlueControlProperty... blueControlProperties) {
            this.propertyDao.delete(blueControlProperties[0]);
            return null;
        }
    }


    public void deletePropertiesForControlBy(int controlId){
        this.propertyDao.deletePropertiesFor(controlId);
    }
    // TODO: delete properties for control by control id.
    public void deletePropertiesForControlBy_LIVE(int controlID){
        new deletePropertiesForControlBy_Async(propertyDao).execute(controlID);
    }
    //TODO: delete properties class.
    private static class deletePropertiesForControlBy_Async extends AsyncTask<Integer, Void, Void>{
        private  BlueControlPropertyDao propertyDao;

        public deletePropertiesForControlBy_Async(BlueControlPropertyDao propertyDao) {
            this.propertyDao = propertyDao;
        }

        @Override
        protected Void doInBackground(Integer... controlID) {
            this.propertyDao.deletePropertiesForControlBy_LIVE(controlID[0]);
            return null;
        }
    }




    public BlueControlPropertyRepository(Application application){
        BlueProfileDatabase db = BlueProfileDatabase.getInstance(application);
        propertyDao = db.blueControlPropertyDao();

    }


    public ArrayList<BlueControlProperty> getPropertiesForControlWith(int id){
        return (ArrayList<BlueControlProperty>) propertyDao.getPropertiesForControlWith(id);
    }





}
