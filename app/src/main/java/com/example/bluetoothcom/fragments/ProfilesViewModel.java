package com.example.bluetoothcom.fragments;

import android.app.Application;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bluetoothcom.data.BlueProfile;
import com.example.bluetoothcom.data.BlueProfileRepository;

import java.util.ArrayList;
import java.util.List;

public class ProfilesViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    private BlueProfileRepository profileRepository;
    private ArrayList<BlueProfile> listOfProfiles;
    public ProfileGridAdapter mAdapterGrid;
    public View mView;
    public GridView mGridView;


    public ProfilesViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new BlueProfileRepository(application);


    }

    public void setListOfProfiles(ArrayList<BlueProfile> listOfProfiles){
        this.listOfProfiles = listOfProfiles;
        if( mAdapterGrid != null)
            mAdapterGrid.setListOfProfiles(this.listOfProfiles);
    }

    public LiveData<List<BlueProfile>> getProfiles(){
        return profileRepository.getProfiles();
    }

    public BlueProfile getDefaultProfile(){
        return  profileRepository.getDefaultProfile();

    }

    public void insertProfile(BlueProfile profile){ profileRepository.insertProfile(profile);}
    public void deleteProfileWith(String name){ profileRepository.deleteProfileWith(name);}


}