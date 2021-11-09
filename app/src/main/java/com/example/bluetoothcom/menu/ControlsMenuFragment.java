package com.example.bluetoothcom.menu;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bluetoothcom.R;
import com.example.bluetoothcom.controls.ControlsMenuAdapter;
import com.example.bluetoothcom.data.BlueControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControlsMenuFragment extends Fragment {

    private ArrayList<BlueControl> mListOfControls;
    private GridView mGridView;
    private ControlsMenuViewModel mViewModel;
    private ControlsMenuAdapter mAdapter;

    private View mView;
    private View tempView;
    public static ControlsMenuFragment newInstance() {
        return new ControlsMenuFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = tempView;


        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float parentHeight =((ViewGroup)v.getParent()).getHeight();
                if( event.getY() < 100 && event.getAction() == MotionEvent.ACTION_DOWN){

                    if( parentHeight - v.getY() > 300){
                        hideMenu(true);
                    }else if(  event.getAction() == MotionEvent.ACTION_DOWN) {
                        hideMenu(false);
                    }

                }
                return true;
            }
        });


        mListOfControls = new ArrayList<>();
        mListOfControls = new ArrayList<>();


        mAdapter = new ControlsMenuAdapter(mView.getContext(),mListOfControls);
        mGridView = mView.findViewById(R.id.controls_menu_grid);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView title = view.findViewById(R.id.controls_menu_cell_title);
                ImageView image = view.findViewById(R.id.controls_menu_cell_image);
                ClipData.Item item = new ClipData.Item(title.getText());
//                System.out.println("title = " + title.getText());

                String[] mime = new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData drag = new ClipData(title.getText(), mime,item);

                View.DragShadowBuilder shadow = new DragShadow(image);

                image.startDragAndDrop(drag,shadow,null,0);

                return true;
            }
        });

//        mViewModel = new ViewModelProvider(this).get(ControlsMenuViewModel.class);
//
//        mViewModel.getDefaultControls().observe(this, new Observer<List<BlueControl>>() {
//            @Override
//            public void onChanged(List<BlueControl> blueControls) {
//
//                for( BlueControl control: blueControls){
//                    System.out.println("Control = " + control.getName());
//                }
//            }
//        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        tempView= inflater.inflate(R.layout.controls_menu_fragment, container, false);

       return tempView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ControlsMenuViewModel.class);
        // TODO: Use the ViewModel


        mListOfControls = (ArrayList<BlueControl>) mViewModel.getDefaultControls();
        mAdapter.setListOfControls(mListOfControls);


        mListOfControls = (ArrayList<BlueControl>) mViewModel.getDefaultControls();
        if(mListOfControls != null)
            mAdapter.setListOfControls(mListOfControls);


        //TODO: LiveData example.
//        mViewModel.getDefaultControls().observe(this, new Observer<List<BlueControl>>() {
//            @Override
//            public void onChanged(List<BlueControl> blueControls) {
//            }
//        });


    }

    public void hideMenu(Boolean yes){
        View v = mView;

        float parentHeight = ((ViewGroup)v.getParent()).getHeight();

        SpringAnimation sp = new SpringAnimation(v, DynamicAnimation.Y);
        SpringForce spf = new SpringForce();
        spf.setStiffness(SpringForce.STIFFNESS_LOW);
        spf.setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);

        spf.setFinalPosition(parentHeight - 280);
        if( !yes){
            spf.setFinalPosition(parentHeight - (v.getHeight() - 50));
        }
        sp.setSpring(spf);
        sp.start();

    }


    private static class DragShadow extends View.DragShadowBuilder{

        private static ImageView image;

        public DragShadow(View v){
            super(v);

            image = v.findViewById(R.id.controls_menu_cell_image);
        }


        @Override
        public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {

            super.onProvideShadowMetrics(outShadowSize, outShadowTouchPoint);

            int width, height;

            width = getView().getWidth() ;

            height = getView().getHeight() ;

//            shadow.setBounds(0,0,width,height);

            outShadowSize.set(width,height);

            outShadowTouchPoint.set(width/2,height/2);

        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            super.onDrawShadow(canvas);
            image.draw(canvas);
        }
    }
}