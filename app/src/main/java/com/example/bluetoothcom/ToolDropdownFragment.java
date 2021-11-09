package com.example.bluetoothcom;

import androidx.lifecycle.ViewModelProvider;

import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class ToolDropdownFragment extends Fragment implements ToolDropdownAdapter.OnDropdownItemClickListener {

    private ToolDropdownAdapter adapter;
    private int menuHeight = 100;
    private EditText mTextField;
    public View mView;

    @Nullable
    @Override
    public View getView() {
        return mView;
    }


    private ToolDropdownViewModel mViewModel;

    public static ToolDropdownFragment newInstance() {
        return new ToolDropdownFragment();
    }
    private OnDropdownItemItemChangeListener dropdownChangeListener;
    public interface OnDropdownItemItemChangeListener {
        void onDropdownChange(int fieldId,ToolDropdownFragment.DropdownItem item);
    }

    public void setDropdownChangeListener(OnDropdownItemItemChangeListener listener){
        this.dropdownChangeListener = listener;
    }
    // TODO: menu needs to be self contained. implement interface.
    public void setTextField(EditText textField){
        this.mTextField = textField;
        ViewGroup.LayoutParams lp = mView.getLayoutParams();
        lp.width = mTextField.getWidth() + 150;
        mView.setLayoutParams(lp);
        mView.setX(mTextField.getX());
        mView.setY(mTextField.getY() + mTextField.getHeight() - 1);

    }
    private void resizeMenu(){
        int height = this.menuHeight;
        ValueAnimator vay =  ValueAnimator.ofInt(height);
        //System.out.println("size + " + String.valueOf(height));
        vay.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    ViewGroup.LayoutParams lp = mView.getLayoutParams();
                    lp.height = (int) animation.getAnimatedValue();

                    mView.setLayoutParams(lp);

                }catch (Exception e){}
            }
        });
        vay.setDuration(200);
        vay.start();

    }

    public void setItemList(ArrayList<DropdownItem> listOfItems, boolean removeDuplicate){
        adapter.setListOfItems(listOfItems,removeDuplicate);
    }
    public void addItem(DropdownItem item, boolean removeDuplicates ){
        adapter.addItem(item, removeDuplicates);
    }

    public void setMenuHeight(int height){
        this.menuHeight = height;
        resizeMenu();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tool_dropdown_fragment, container, false);

        RecyclerView rv = mView.findViewById(R.id.tool_dropdown);
        adapter = new ToolDropdownAdapter(ToolDropdownFragment.this);
        rv.setAdapter(adapter);
        rv.setLayoutManager( new LinearLayoutManager(mView.getContext()));
        resizeMenu();

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ToolDropdownViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDropdownClick( DropdownItem item) {
        //TODO: onDropdownClick from ToolDropdownAdapter
        //System.out.println(item.getName());
        if(dropdownChangeListener != null)
            dropdownChangeListener.onDropdownChange(mTextField.getId(), item);

        mTextField.setText(item.getName());

        ViewGroup.LayoutParams lp = mView.getLayoutParams();
        lp.height = 100;
        mView.setLayoutParams(lp);
    }


    static public class DropdownItem{

        public DropdownItem(String name, String displayName, String id) {
            this.name = name;
            this.id = id;
            this.displayName = displayName;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getName() {

            return name;
        }

        public String getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }

        String name = "";
        String id = "";
        String displayName = "";

    }

}