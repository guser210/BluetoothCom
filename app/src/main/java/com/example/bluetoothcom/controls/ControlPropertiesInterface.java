package com.example.bluetoothcom.controls;

import com.example.bluetoothcom.data.BlueControlProperty;

import java.util.List;

public interface ControlPropertiesInterface {
    void onDelete();
    void onCancel();

    void onOk(List<BlueControlProperty> listOfProperties);
}


