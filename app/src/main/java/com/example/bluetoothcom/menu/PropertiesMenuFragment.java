package com.example.bluetoothcom.menu;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.bluetoothcom.R;
import com.example.bluetoothcom.controls.ControlPropertiesInterface;
import com.example.bluetoothcom.controls.PropertiesMenuAdapter;
import com.example.bluetoothcom.data.BlueControlProperty;

import org.w3c.dom.Text;

import java.util.List;

public class PropertiesMenuFragment extends Fragment {
    private View mView;
    private PropertiesMenuViewModel mViewModel;
    float startX=0, startY=0;
    private ControlPropertiesInterface buttonActions;

    private List<BlueControlProperty> mListOfProperties;
    private PropertiesMenuAdapter mAdapter;

    public static PropertiesMenuFragment newInstance() {
        return new PropertiesMenuFragment( );
    }

    public void setProperties(List<BlueControlProperty> listOfProperties){
        mAdapter.setListOfProperties(listOfProperties);
    }

    public void setTitle(String title){
        TextView titleField  = mView.findViewById(R.id.properties_menu_title);
        titleField.setText(title);
    }
    public void setDescription(String description){
        TextView descriptionField = mView.findViewById(R.id.properties_menu_description);
        descriptionField.setText(description);
    }

    @Nullable
    @Override
    public View getView() {
        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if( context instanceof ControlPropertiesInterface) {
            buttonActions = (ControlPropertiesInterface) context;
        }
    }
    public void hideKeyboard(){

        InputMethodManager im = (InputMethodManager) mView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);// getParentFragment().getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(mView.getWindowToken(),0);
    }
    public void setListener(ControlPropertiesInterface listener){
        this.buttonActions = listener;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.properties_menu_fragment, container, false);

        RecyclerView recyclerView = mView.findViewById(R.id.control_properties);
        mAdapter = new PropertiesMenuAdapter(mListOfProperties);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));



        ((Button) mView.findViewById(R.id.control_properties_button_delete))
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                buttonActions.onDelete();

            }
        });
        ((Button) mView.findViewById(R.id.control_properties_button_cancel))
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                buttonActions.onCancel();

            }
        });
        ((Button) mView.findViewById(R.id.control_properties_button_ok))
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonActions.onOk(mAdapter.getListOfProperties());//mListOfProperties);
                hideKeyboard();
            }
        });

        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                hideKeyboard();

                //System.out.println("Action = " + event.getAction());
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
                        }

                        break;
                }


                return true;

            }
        });
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PropertiesMenuViewModel.class);
        // TODO: Use the ViewModel
    }


}