package com.example.bluetoothcom.controls;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bluetoothcom.R;
import com.example.bluetoothcom.data.BlueControlProperty;

import java.util.ArrayList;
import java.util.List;

public class ControlOdometerV2Fragment extends ControlComm implements ControlPropertiesInterface {

    private ControlOdometerV2ViewModel mViewModel;
    private View mView;
    private TextView mGear;
    private ControlOdometerV2_1 mOdometer;
    private GestureDetector mGestureDetector;
    private String mGearDelimiter;
    private String mRPMDelimiter;


    public static ControlOdometerV2Fragment newInstance() {
        return new ControlOdometerV2Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.control_odometer_v2_fragment, container, false);
        mCommView = mView;

        setControlName("Odometer");
        mGear = mView.findViewById(R.id.control_odometer_gear);
        mOdometer = mView.findViewById(R.id.control_odometer);
        mGear.setText("N");
        mView.setScaleY(1.0f);
        mView.setScaleX(0.5f);
        ConstraintLayout cl = mView.findViewById(R.id.control_odometer_needle_group);
        mOdometer.setNeedleGroup(cl);

        mCommView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View button, MotionEvent event) {

                mGestureDetector.onTouchEvent(event);
                return moveMe(mCommView, event,false);

            }
        });

        mGestureDetector = new GestureDetector(mCommView.getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {

                ViewGroup vg = (ViewGroup) mCommView.getParent();
                if( ismLocked()){ return super.onDoubleTap(e);}

                setInflater(inflater);
                showPropertiesMenu(ControlOdometerV2Fragment.this,"Odometer properties","Set a custom properties for Odometer.");
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e); // this conflicts with moving controls.
            }
        });

        return  mView;
    }

    void dismissPropertiesMenu(){
        try {
            ViewGroup vg = (ViewGroup) mCommView.getParent();
            vg.removeView(mPropertiesMenu.getView());
            mPropertiesMenu = null;
        }catch (Exception ex){

        }
        hideKeyboard();

    }
    @Override
    public void onDelete() {
        ViewGroup vg = (ViewGroup) mCommView.getParent();
        vg.removeView(mPropertiesMenu.getView());
        if( eventsInterface != null)
            eventsInterface.onDelete(this);

        dismissPropertiesMenu();

    }

    @Override
    public void onCancel() {
        dismissPropertiesMenu();    }

    @Override
    public void onOk(List<BlueControlProperty> listOfProperties) {
        //TODO: add settings update
        setProperties((ArrayList<BlueControlProperty>) listOfProperties);

        dismissPropertiesMenu();    }

    @Override
    public void setProperties(ArrayList<BlueControlProperty> listOfProperties) {
        //TODO: ControlComm
        super.setProperties(listOfProperties);

        notifyPropertiesChanged();
    }

    public void notifyPropertiesChanged(){
        //TODO: property names

        TextView label = mCommView.findViewById(R.id.control_status_label_label);



        BlueControlProperty propertyScaleX = getPropertyBy("ScaleX");
        BlueControlProperty propertyScaleY = getPropertyBy("ScaleY");
        BlueControlProperty propertyTextColor = getPropertyBy("TextColor");
        BlueControlProperty propertyGearTextColor = getPropertyBy("GearTextColor");
        BlueControlProperty propertyBackgroundColor = getPropertyBy("Background");

        BlueControlProperty propertyRingColor = getPropertyBy("RingColor");
        BlueControlProperty propertyScaleColor = getPropertyBy("ScaleColor");

        BlueControlProperty propertyRPM = getPropertyBy("RPM");
        BlueControlProperty propertyGear = getPropertyBy("Gear");

        mView.setScaleX( propertyScaleX.getValueInt() * 0.01f);
        mView.setScaleY( propertyScaleY.getValueInt() * 0.01f);


        try{
            setData(propertyGear.getValue() + propertyGear.getDelimiterValue() + propertyGear.getDefaultValue());
            setData(propertyRPM.getValue() + propertyRPM.getDelimiterValue() + propertyRPM.getDefaultValue());

        }catch (Exception ex){

        }

        mRPMDelimiter = propertyRPM.getDelimiterString();
        mGearDelimiter = propertyGear.getDelimiterString();
        try {
            int color = (int)Long.parseLong(propertyTextColor.getValue(), 16);
            mOdometer.setFontColor(color);
            mOdometer.setBackgroundColor((int)Long.parseLong(propertyBackgroundColor.getValue(), 16) );
            mOdometer.setRingColor((int)Long.parseLong(propertyRingColor.getValue(), 16) );
            mOdometer.setScaleColor((int)Long.parseLong(propertyScaleColor.getValue(), 16) );


        }catch (Exception ex){
            System.out.println("ERR setFontColor = " + ex.getMessage());
        }



        try {
            TextView gear = mView.findViewById(R.id.control_odometer_gear);
            gear.setTextColor((int)Long.parseLong(propertyGearTextColor.getValue(), 16) );

        }catch (Exception ex){}

        mOdometer.invalidate();
        hideKeyboard();
    }
    @Override
    public void setData(String data) {
        super.setData(data);

        String[] GearField = data.split(mGearDelimiter);
        String[] RpmField = data.split(mRPMDelimiter);
        if( GearField.length < 2) return;

        if(GearField.length > 1 && GearField[0].equals("GEAR"))
            mGear.setText(  GearField[1]);

        if( RpmField.length >1 &&  RpmField[0].equals("RPM")) {
            try {
                mOdometer.setValue(Integer.valueOf(RpmField[1]));
            }
            catch (Exception ex){}
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ControlOdometerV2ViewModel.class);
        // TODO: Use the ViewModel
    }



}