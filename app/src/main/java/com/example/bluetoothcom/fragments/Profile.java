package com.example.bluetoothcom.fragments;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.example.bluetoothcom.ToolDropdownFragment;
import com.example.bluetoothcom.data.BlueProfile;

import java.util.ArrayList;
import java.util.Optional;

public class Profile {
     String profileName = "";
     ToolDropdownFragment.DropdownItem mProfileItem;
     ToolDropdownFragment.DropdownItem mDeviceItem;
     ToolDropdownFragment.DropdownItem mServiceItem;
     ToolDropdownFragment.DropdownItem mCharacteristicItem;

     BluetoothGattCharacteristic mCharacteristic;
     ArrayList<BlueProfile> mListOfProfiles = new ArrayList<>();
     ArrayList<BluetoothDevice> mListOfDevices = new ArrayList<>();
     ArrayList<BluetoothGattService> mListOfServices = new ArrayList<>();
     ArrayList<BluetoothGattCharacteristic> mListOfCharacteristics = new ArrayList<>();
    //public DeviceConnection mDeviceConnection = new DeviceConnection();

    public void clearProfiles(){ mListOfProfiles.clear();}
    public void clearDevices(){ mListOfDevices.clear();}
    public void clearServices(){ mListOfServices.clear(); }
    public void clearCharacteristics(){mListOfCharacteristics.clear();}

    public void addProfile(BlueProfile profile){ mListOfProfiles.add(profile);}
    public void addDevice(BluetoothDevice device){ mListOfDevices.add(device);}
    public void addService(BluetoothGattService service){ mListOfServices.add(service);}
    public void addCharacteristic(BluetoothGattCharacteristic characteristic){ mListOfCharacteristics.add(characteristic);}


    public Optional<BlueProfile> findProfile( String name){
        return mListOfProfiles.stream().filter(d ->d.getName().equals(name)).findFirst();
    }
    public Optional<BluetoothDevice> findDevice( String address){
        return mListOfDevices.stream().filter(d ->d.getAddress().toString().equals(address)).findFirst();
    }

    public Optional<BluetoothGattService> findService( String address){
        return mListOfServices.stream().filter(d ->d.getUuid().toString().equals(address)).findFirst();
    }

    public Optional<BluetoothGattCharacteristic> findCharacteristic( String address){
        return mListOfCharacteristics.stream().filter(d ->d.getUuid().toString().equals(address)).findFirst();
    }

    public Optional<BluetoothGattCharacteristic> findCharacteristic( ToolDropdownFragment.DropdownItem characteristic){
        if( characteristic == null) return Optional.empty();

        return mListOfCharacteristics.stream().filter(d ->d.getUuid().toString().equals(characteristic.getId()) ).findFirst();
    }


    public void setListOfProfiles(ArrayList<BlueProfile> mListOfProfiles) {
        this.mListOfProfiles = mListOfProfiles;
    }

    public void setListOfDevices(ArrayList<BluetoothDevice> mListOfDevices) {
        this.mListOfDevices = mListOfDevices;
    }

    public void setListOfServices(ArrayList<BluetoothGattService> mListOfServices) {
        this.mListOfServices = mListOfServices;
    }

    public void setListOfCharacteristics(ArrayList<BluetoothGattCharacteristic> mListOfCharacteristics) {
        this.mListOfCharacteristics = mListOfCharacteristics;
    }

    public void setProfile(ToolDropdownFragment.DropdownItem mProfileItem) {
        this.mProfileItem = mProfileItem;
    }

    public void setDevice(ToolDropdownFragment.DropdownItem mDeviceItem) {
        this.mDeviceItem = mDeviceItem;
    }

    public void setService(ToolDropdownFragment.DropdownItem mServiceItem) {
        this.mServiceItem = mServiceItem;
    }

    public void setCharacteristic(ToolDropdownFragment.DropdownItem mCharacteristicItem) {
        this.mCharacteristicItem = mCharacteristicItem;
    }

    public ToolDropdownFragment.DropdownItem getProfile() {
        return mProfileItem;
    }

    public ToolDropdownFragment.DropdownItem getDevice() {
        return mDeviceItem;
    }

    public ToolDropdownFragment.DropdownItem getService() {
        return mServiceItem;
    }

    public ToolDropdownFragment.DropdownItem getCharacteristic() {
        return mCharacteristicItem;
    }


    public ArrayList<BlueProfile> getListOfProfiles() {
        return mListOfProfiles;
    }

    public ArrayList<BluetoothDevice> getListOfDevices() {
        return mListOfDevices;
    }

    public ArrayList<BluetoothGattService> getListOfServices() {
        return mListOfServices;
    }

    public ArrayList<BluetoothGattCharacteristic> getListOfCharacteristics() {
        return mListOfCharacteristics;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileName() {
        return profileName == null ? "" : profileName;
    }
}
