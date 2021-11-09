package com.example.bluetoothcom.controls;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

import com.example.bluetoothcom.ToolDropdownFragment;

public interface SettingsMenuInterface  {
//    void settingsCommOnProfileChanged(String profileName);
//    void settingsCommOnDeviceNameChanged(String deviceName);
//    void settingsCommOnServiceChanged(String serviceName);
//    void settingsCommOnCharacteristicChanged(String characteristicName);

    void onMenuChange(String menu, ToolDropdownFragment.DropdownItem item);
    void onMenuOpen(String menu, String value);
    void onMenuClose(String menu, String value);
    void onStartScan();
    void onStopScan();
    void onConnectToDevice(ToolDropdownFragment.DropdownItem item);
    void onProfileSettingsMenuOkClicked();
    void onTakeImage(Bitmap profileImage);
}
