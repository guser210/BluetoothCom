package com.example.bluetoothcom.controls;

import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bluetoothcom.menu.PropertiesMenuFragment;
import com.example.bluetoothcom.R;
import com.example.bluetoothcom.data.BlueControlProperty;

import java.util.ArrayList;
import java.util.List;

public class ControlBigButtonFragment extends ControlComm  implements ControlPropertiesInterface{

    private ControlBigButtonViewModel mViewModel;
    private GestureDetector mGestureDetector;
    private ControlEventInterface eventsInterface;
    private BlueControlProperty propertyCommand;

    public static ControlBigButtonFragment newInstance() {
        return new ControlBigButtonFragment();
    }

    @Override
    public void setEventListener(ControlEventInterface listener) {
        super.setEventListener(listener);
        this.eventsInterface = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mCommView =  inflater.inflate(R.layout.controls_big_button_fragment, container, false);

        setControlName("BigButton");

        Button button = mCommView.findViewById(R.id.controls_big_button_button);

        mGestureDetector = new GestureDetector(mCommView.getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }


            @Override
            public boolean onDoubleTap(MotionEvent e) {

                if( ismLocked()){ return super.onDoubleTap(e);}

                setInflater(inflater);
                showPropertiesMenu(ControlBigButtonFragment.this,"Button properties","you can change the button title, command name, Text size, and value.");
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e); // this conflicts with moving controls.
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(propertyCommand == null) return;
                if( eventsInterface != null)
                    eventsInterface.controlDataToDevice( propertyCommand.getValue() + propertyCommand.getDelimiterValue() + propertyCommand.getDefaultValue());
            }
        });

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View button, MotionEvent event) {

                mGestureDetector.onTouchEvent(event);
                return moveMe(mCommView, event,true);

            }
        });


        return mCommView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ControlBigButtonViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void setData(String data) {
        super.setData(data);
    }

    @Override
    public String getControlName() {
        return "BigButton";
    }

    @Override
    public View getCommView() {
        return super.getCommView();
    }

    @Override
    public void onDelete() {
        ViewGroup vg = (ViewGroup) mCommView.getParent();
        vg.removeView(mPropertiesMenu.getView());
        mPropertiesMenu = null;
        mGestureDetector = null;
        if( eventsInterface != null)
            eventsInterface.onDelete(this);

        hideKeyboard();
    }

    @Override
    public void onCancel() {
        ViewGroup vg = (ViewGroup) mCommView.getParent();
        vg.removeView(mPropertiesMenu.getView());
        mPropertiesMenu = null;
        hideKeyboard();
    }

    @Override
    public void onOk(List<BlueControlProperty> listOfProperties) {
        // TODO: update properties list.
        setProperties((ArrayList<BlueControlProperty>) listOfProperties);

        ViewGroup vg = (ViewGroup) mCommView.getParent();
        vg.removeView(mPropertiesMenu.getView());
        mPropertiesMenu = null;

        hideKeyboard();
    }
    @Override
    public void setProperties(ArrayList<BlueControlProperty> listOfProperties) {
        //TODO: ControlComm
        super.setProperties(listOfProperties);

        notifyPropertiesChanged();
    }

    public void notifyPropertiesChanged(){
        //TODO: property names

        Button button = mCommView.findViewById(R.id.controls_big_button_button);

        BlueControlProperty propertyTitle = getPropertyBy("ButtonTitle");
        BlueControlProperty propertyFontSize = getPropertyBy("TextSize");
        propertyCommand = getPropertyBy("ButtonCommand");

        if( propertyFontSize != null){ button.setTextSize(propertyFontSize.getValueFloat()); }
        if( propertyTitle != null){ button.setText(propertyTitle.getValue()); }

        hideKeyboard();
    }
}