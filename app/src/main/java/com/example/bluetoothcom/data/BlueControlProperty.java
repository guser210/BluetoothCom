package com.example.bluetoothcom.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BlueControlProperty {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int controlID;
    private int propertyType;
    private String propertyName;
    private int sort;
    private String label;
    private String value;
    private String delimiterValue;
    private int delimiterType;
    private String defaultLabel;
    private String defaultValue;



    public int getId() {
        return id;
    }

    public int getControlID() {
        return controlID;
    }

    public int getPropertyType() {
        return propertyType;
    }

    public String getPropertyName(){ return propertyName; }
    public void setPropertyName(String propertyName){ this.propertyName = propertyName;}

    public int getSort() {
        return sort;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public float getValueFloat(){

        try{
            return Float.parseFloat(value);
        }
        catch (Exception ex){

        }

        return 0;
    }

    public int getValueInt(){
        try{
            return Integer.parseInt(value);
        }catch (Exception ex){}

        return 0;
    }

    public String getDelimiterValue() {
        //return getDelimiter();
        return delimiterValue;
    }


    public int getDelimiterType() {
        return delimiterType;
    }

    public String getDefaultLabel() {
        return defaultLabel;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setControlID(int controlID) {
        this.controlID = controlID;
    }

    public void setPropertyType(int propertyType) {
        this.propertyType = propertyType;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(String value) {
        this.value = value;
    }



    public String getDelimiterString(){

//        System.out.println("from string value: " + (char)Integer.valueOf(b).intValue());
//        System.out.println("from string = " + String.valueOf((int)a.toCharArray()[0]));
        if( propertyType != 3) return ":";
        if(delimiterValue == "") return ":"; // TODO: default delimiter.

        if( delimiterType == 1){
            return delimiterValue;
        }
        else {
            //mDelimiter = String.valueOf((char)65);
            return String.valueOf((char)Integer.valueOf(delimiterValue).intValue());
            //return  String.valueOf(delimiterValue.toCharArray()[0]);
        }
    }

    public void setDelimiterValue(String delimiterValue) {
        this.delimiterValue = delimiterValue;
    }

    public void setDelimiterType(int delimiterType) {
        this.delimiterType = delimiterType;
    }

    public void setDefaultLabel(String defaultLabel) {
        this.defaultLabel = defaultLabel;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public BlueControlProperty(int id
                               , int controlID
                               , int propertyType
                               ,String propertyName
                               , int sort
                               , String label
                               , String value
                               , String delimiterValue
                               , int delimiterType
                               , String defaultLabel
                               , String defaultValue
                                ) {
        this.id = id;
        this.controlID = controlID;
        this.propertyType = propertyType;
        this.propertyName = propertyName;
        this.sort = sort;
        this.label = label;
        this.value = value;
        this.delimiterValue = delimiterValue;
        this.delimiterType = delimiterType;
        this.defaultLabel = defaultLabel;
        this.defaultValue = defaultValue;

    }


}
