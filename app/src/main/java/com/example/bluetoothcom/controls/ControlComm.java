package com.example.bluetoothcom.controls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.bluetoothcom.data.BlueControlProperty;
import com.example.bluetoothcom.menu.PropertiesMenuFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ControlComm extends Fragment  {

    public PropertiesMenuFragment mPropertiesMenu;
    boolean mLocked = false;
    boolean echo = true;
    float x;
    float y;
    float width;
    float height;
    String controlName = "getControlName() not implemented";
    public ControlEventInterface eventsInterface;


    public List<BlueControlProperty> mCommListOfProperties;
    public View mCommView;
    private LayoutInflater mInflater;
    private float startX;
    private float startY;

    public void setEcho(boolean echo) {
        this.echo = echo;
    }

    public boolean isEcho() {
        return echo;
    }

    public void lockControl(){
        mLocked = true;
    }
    public void unlockControl(){
        mLocked = false;
    }
    public boolean ismLocked(){return  mLocked;}

    public void setInflater(LayoutInflater inflater){
        this.mInflater = inflater;
    }
    public void showPropertiesMenu(ControlPropertiesInterface propertiesInterface, String title, String Description){
        ViewGroup vg = (ViewGroup) mCommView.getParent();

        if( mPropertiesMenu == null) {

            mPropertiesMenu = new PropertiesMenuFragment();

            mPropertiesMenu.setListener(propertiesInterface);

            View pview = mPropertiesMenu.onCreateView(mInflater,vg,null);
            pview.setLayoutParams(new LinearLayout.LayoutParams(800,1000));

            vg.addView(mPropertiesMenu.getView());

            mPropertiesMenu.setTitle(title);
            mPropertiesMenu.setDescription("you can change the button title, command name, Text size, and value.");

            pview.setX(mCommView.getX() );
            pview.setY(300);
            mPropertiesMenu.setProperties(mCommListOfProperties);

        }else
        {
        }
    }
    public boolean moveMe(View v, MotionEvent event, boolean returnFalseOnNotMoving){

        if(mLocked) return false;
        //System.out.println("Event: " + event.getAction() + " -- " + MotionEvent.ACTION_DOWN);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX, endY;

                endX = event.getX();
                endY = event.getY();
                if( startX != -1) {
                    float distanceX = endX - startX;
                    float distanceY = endY - startY;

                    v.setX(v.getX() + distanceX);
                    v.setY(v.getY() + distanceY);
                    setX(v.getX());
                    setY(v.getY());
                }

                break;
        }

        if( event.getAction() != MotionEvent.ACTION_MOVE && returnFalseOnNotMoving)return false;

        return true;
    }

    public void setData(String data){

    }

    public BlueControlProperty getPropertyBy(String name){
        Optional<BlueControlProperty> property = mCommListOfProperties.stream().filter(p -> p.getPropertyName().equals(name)).findFirst();
        if( property.isPresent())
            return property.get();
        return null;
    }
    public void setProperties(ArrayList<BlueControlProperty> listOfProperties){
        mCommListOfProperties = listOfProperties;
    }
    public void setEventListener(ControlEventInterface listener){
        eventsInterface = listener;
    }

    public void hideKeyboard(){
        InputMethodManager im = (InputMethodManager) mCommView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);// getParentFragment().getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(mCommView.getWindowToken(),0);
    }
    public String getControlName(){
        return this.controlName;
    }
    public View getCommView(){
        return mCommView;
    }
    public void setControlName(String controlName){
        this.controlName = controlName;
    }

    public boolean isCommLocked() {
        return mLocked;
    }

    public void setCommLocked(boolean commLocked) {
        this.mLocked = commLocked;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}


