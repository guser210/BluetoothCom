package com.example.bluetoothcom.menu;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bluetoothcom.data.BlueControl;
import com.example.bluetoothcom.data.BlueControlRepository;
import com.example.bluetoothcom.data.BlueProfile;
import com.example.bluetoothcom.data.BlueProfileRepository;

import java.util.List;

public class ControlsMenuViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private BlueControlRepository controlRepository;

    public ControlsMenuViewModel(@NonNull Application application) {
        super(application);
        controlRepository = new BlueControlRepository(application);

    }


    public LiveData<List<BlueControl>> getProfiles(){
        return null;
    }

    public List<BlueControl> getDefaultControls(){
        return controlRepository.getDefaultControls();

    }

    //public void insertControl(BlueControl control){ controlRepository.insertControl(control);}
    //public void deleteControlFrom(String profileName, String controlname){ controlRepository.deleteControlFrom(controlname,profileName);}

}