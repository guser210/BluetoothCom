package com.example.bluetoothcom.controls;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
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

public class ControlConsoleFragment extends ControlComm  implements ControlPropertiesInterface {

    private ControlConsoleViewModel mViewModel;
    private ControlConsoleAdapter mAdapter;
    private RecyclerView recyclerView;
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;
    private double mScale = 1.0;
    private boolean mScaling = false;

    private Context mContext;

    private LayoutInflater minflater;
    public static ControlConsoleFragment newInstance() {
        return new ControlConsoleFragment();
    }


    float startX=0, startY=0;
    long timeOfClick = 0;
    @Override
    public void setData(String data) {
        super.setData(data);
        mAdapter.addLine(data);
        if(mAdapter.mPaused == false)
            recyclerView.smoothScrollToPosition(1000);
    }


    @Override
    public View getCommView() {
        return super.getCommView();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mCommView =  inflater.inflate(R.layout.controls_console_fragment, container, false);

        setControlName("Console");

        mContext = this.getContext();
        recyclerView = (RecyclerView) mCommView.findViewById(R.id.control_console_rv);

        minflater = inflater;
        mAdapter = new ControlConsoleAdapter();

        recyclerView.setAdapter(mAdapter);


        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        Button buttonPause = mCommView.findViewById(R.id.console_button_pause);
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView title = v.findViewById(R.id.console_button_pause);
                if( title.getText().toString().compareTo("Continue") == 0){
                    title.setText("Pause");
                    mAdapter.mPaused = false;
                }
                else
                {
                    title.setText("Continue");
                    mAdapter.mPaused = true;

                }
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

                setInflater(inflater);
                showPropertiesMenu(ControlConsoleFragment.this,"Console Properties","Displays raw data from connected bluetooth device and controls on the same screen.");
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e); // this conflicts with moving controls.
            }
        });

        mScaleDetector = new ScaleGestureDetector(mCommView.getContext(), new ScaleGestureDetector.OnScaleGestureListener() {


            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                mScale *= detector.getScaleFactor();

                ViewGroup.LayoutParams lp = mCommView.getLayoutParams();
                lp.height = (int) (getHeight() * mScale);
                lp.width = (int) (getWidth() * mScale);
                mCommView.setLayoutParams(lp);
                setHeight(lp.height);
                setWidth(lp.width);
                mScale = 1.0;
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                //System.out.println("onScaleBegin " + detector);
                mScaling = true;
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                //System.out.println("onScaleEnd " + detector);
                mScaling = false;
            }
        });


        mCommView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(mLocked) return false;
                mGestureDetector.onTouchEvent(event);
                mScaleDetector.onTouchEvent(event);
                if(mScaling){
                    startX = -1;
                    return false;
                }

                return moveMe(v,event,false);

            }
        });

        mAdapter.addLine("Console");
        mAdapter.addLine("Echoes commands");
        mAdapter.addLine("from local controls");
        mAdapter.addLine("");

        mAdapter.addLine("Displays");
        mAdapter.addLine("line data");
        mAdapter.addLine("terminated by \\r");
        mAdapter.addLine("from connected device.");

        mCommView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //System.out.println(left);
            }
        });
        return mCommView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ControlConsoleViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDelete() {
        ViewGroup vg = (ViewGroup) mCommView.getParent();
        vg.removeView(mPropertiesMenu.getView());
        mPropertiesMenu = null;
        hideKeyboard();
        if( eventsInterface != null)
            eventsInterface.onDelete(this);


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
        setProperties((ArrayList<BlueControlProperty>) listOfProperties);

        ViewGroup vg = (ViewGroup) mCommView.getParent();
        vg.removeView(mPropertiesMenu.getView());
        mPropertiesMenu = null;

        hideKeyboard();

    }
    @Override
    public void setProperties(ArrayList<BlueControlProperty> listOfProperties) {
        super.setProperties(listOfProperties);
        notifyPropertiesChanged();
    }

    public void notifyPropertiesChanged(){
        //TODO: property names
        TextView consoleTile = mCommView.findViewById(R.id.control_console_title);

        BlueControlProperty propertyTitle = getPropertyBy("ConsoleTitle");
        BlueControlProperty propertyTextSize = getPropertyBy("TextSize");
        BlueControlProperty propertyMaxLines = getPropertyBy("MaxLines");
        BlueControlProperty propertyEcho = getPropertyBy("EchoControls");

        if( propertyTitle != null){ consoleTile.setText(propertyTitle.getValue()); }

        if( propertyTextSize != null){ mAdapter.setTextSize(propertyTextSize.getValueInt()); }
        if( propertyMaxLines != null){ mAdapter.setMaxLines(propertyMaxLines.getValueInt()); }

        if( propertyEcho != null){ setEcho(propertyEcho.getValueInt() == 1); }

        hideKeyboard();
    }
}