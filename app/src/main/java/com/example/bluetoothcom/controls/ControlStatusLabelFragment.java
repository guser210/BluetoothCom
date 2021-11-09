package com.example.bluetoothcom.controls;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bluetoothcom.R;
import com.example.bluetoothcom.data.BlueControlProperty;
import com.example.bluetoothcom.menu.PropertiesMenuFragment;

import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class ControlStatusLabelFragment extends ControlComm implements ControlPropertiesInterface {

    private ControlStatusLabelViewModel mViewModel;
    private GestureDetector mGestureDetector;


    private String mDelimiter = ":";
    private String mFieldName = "notset";
    private TextView mFieldValue;

    public static ControlStatusLabelFragment newInstance() {
        return new ControlStatusLabelFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mCommView = inflater.inflate(R.layout.control_status_label_fragment, container, false);

        setControlName("StatusLabel");

        mFieldValue = mCommView.findViewById(R.id.control_status_label_value);


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
                showPropertiesMenu(ControlStatusLabelFragment.this,"Status properties","Set a custom label and display data from connected device.");
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e); // this conflicts with moving controls.
            }
        });
        return mCommView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ControlStatusLabelViewModel.class);
        // TODO: Use the ViewModel
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



        BlueControlProperty propertyTitle = getPropertyBy("Label");
        BlueControlProperty propertyFontSize = getPropertyBy("TextSize");
        BlueControlProperty propertyValueFontSize = getPropertyBy("ValueTextSize");
        BlueControlProperty propertyWidth = getPropertyBy("Width");
        BlueControlProperty propertyValue = getPropertyBy("Value");

        if( propertyValue != null) {
                mDelimiter = propertyValue.getDelimiterValue();
                mFieldName = propertyValue.getValue();
                mFieldValue.setText(propertyTitle.getDefaultValue());

        }


        if( propertyFontSize != null){
            label.setTextSize(propertyFontSize.getValueFloat());
        }
        if( propertyValueFontSize != null){
            mFieldValue.setTextSize(propertyValueFontSize.getValueInt());
        }
        if( propertyTitle != null){ label.setText(propertyTitle.getValue()); }
        if( propertyWidth != null){
            ViewGroup.LayoutParams lp = mCommView.getLayoutParams();

            lp.width = propertyWidth.getValueInt();

        }

        hideKeyboard();
    }

    @Override
    public void setData(String data) {
        super.setData(data);

        String[] field = data.split(mDelimiter);
        if( field.length < 2) return;

        if( field[0].equals(mFieldName))
            mFieldValue.setText(  field[1]);

    }


}