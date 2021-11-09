package com.example.bluetoothcom.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bluetoothcom.data.DataRepository;
import com.example.bluetoothcom.menu.BottomNavigationBarFragment;
import com.example.bluetoothcom.fragments.NewProfileFragment;
import com.example.bluetoothcom.fragments.ProfilesFragment;
import com.example.bluetoothcom.R;
import com.example.bluetoothcom.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ProfilesFragment mProfiles;
    View mProfilesView;
    NewProfileFragment mNewProfiles;
    View mNewProfilesView;

    private View navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        String a = ":";
//        String b = "59";
//
//        System.out.println("from string value: " + (char)Integer.valueOf(b).intValue());
//        System.out.println("from string = " + String.valueOf((int)a.toCharArray()[0]));

        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        //TODO: clear and reload default data, force main controls.
        DataRepository dp = new DataRepository(getApplication());
        dp.clearDefaultData();
        dp.loadDefaultData();

        // Load profiles fragment.
        mProfiles =  new ProfilesFragment();
        mProfilesView = mProfiles.onCreateView(getLayoutInflater(),(ViewGroup) binding.getRoot().getRootView(),savedInstanceState);

        mProfilesView.setLayoutParams(new LinearLayout.LayoutParams(metrics.widthPixels,metrics.heightPixels));
        mProfilesView.setX(0);
        mProfilesView.setY(0);

        ((ViewGroup) binding.getRoot().getRootView()).addView(mProfilesView);


        // Attach profiles fragment to main activity, this is needed to navigate from fragment to activity.
         FragmentTransaction t =  getSupportFragmentManager().beginTransaction();
         t.add(mProfiles,"profilesFragment");
         t.commit();


         // Load New Profiles fragment.
        mNewProfiles =  new NewProfileFragment();
        mNewProfilesView = mNewProfiles.onCreateView(getLayoutInflater(),(ViewGroup) binding.getRoot().getRootView(),savedInstanceState);
        ;
        mNewProfilesView.setLayoutParams(new LinearLayout.LayoutParams(metrics.widthPixels,metrics.heightPixels));
        mNewProfilesView.setX(0);
        mNewProfilesView.setY(0);

        ((ViewGroup) binding.getRoot().getRootView()).addView(mNewProfilesView);
        // Attach profiles fragment to main activity, this is needed to navigate from fragment to activity.
        t =  getSupportFragmentManager().beginTransaction();
        t.add(mNewProfiles,"NewProfilesFragment");
        t.commit();
        loadBottomBar(binding.getRoot().getRootView());





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);


    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return   super.onCreateView(parent, name, context, attrs);

    }


    public void loadBottomBar(View view){
        BottomNavigationBarFragment navigation = new BottomNavigationBarFragment();
        ViewGroup viewGroup = (ViewGroup)view;
        navView = navigation.onCreateView( getLayoutInflater(),viewGroup,null);

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        navView.setLayoutParams(new LinearLayout.LayoutParams(metrics.widthPixels,180));

        navView.setX(0);
        navView.setY( metrics.heightPixels - navView.getLayoutParams().height);

        viewGroup.addView(navView);

        onBottomBarClicked( navView,true);

        navView.findViewById(R.id.bottom_menu_bar_new_profile_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBottomBarClicked((View)v.getParent(),false);
            }
        });

        navView.findViewById(R.id.bottom_menu_bar_profiles_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBottomBarClicked((View)v.getParent(),true);
            }
        });





    }

    public void onBottomBarClicked(View v, boolean profiles){
        View bar = v;

        int colorDefault = 0xFF808080;
        int colorSelected = 0xFF0216CF;

        PorterDuff.Mode modeDefault = PorterDuff.Mode.MULTIPLY;
        int colorIndex = Color.GRAY;


        TextView profilesText = bar.findViewById(R.id.bottom_menu_bar_profiles_title);//.setBackgroundColor(colorDefault);
        TextView newProfileText = findViewById(R.id.bottom_menu_bar_new_profile_title);

        ImageView imageProfiles = findViewById(R.id.bottom_menu_bar_profiles_icon);
        imageProfiles.setColorFilter(colorIndex, modeDefault);

        ImageView imageNewProfile = findViewById(R.id.bottom_menu_bar_new_profile_icon);
        imageNewProfile.setColorFilter(colorIndex, modeDefault);

        profilesText.setTextColor(colorDefault);
        newProfileText.setTextColor(colorDefault);
        ((TextView)navView.findViewById(R.id.bottom_menu_bar_new_profile_title)).setTypeface(null, Typeface.NORMAL);
        ((TextView)navView.findViewById(R.id.bottom_menu_bar_profiles_title)).setTypeface(null, Typeface.NORMAL);

        if( profiles){

            profilesText.setTextColor(colorSelected);
            imageProfiles.clearColorFilter();
            mNewProfilesView.setVisibility(View.GONE);
            mProfilesView.setVisibility(View.VISIBLE);
            setTitle("Saved Profiles");
            ((TextView)navView.findViewById(R.id.bottom_menu_bar_profiles_title)).setTypeface(null, Typeface.BOLD);
            try {
                mNewProfiles.setVisisble(false);
                mProfiles.setVisisble(true);
            }catch (Exception e){

            }
        }
        else{
            newProfileText.setTextColor(colorSelected);
            imageNewProfile.clearColorFilter();
            mNewProfilesView.setVisibility(View.VISIBLE);
            mProfilesView.setVisibility(View.GONE);
            setTitle("Create");
            ((TextView)navView.findViewById(R.id.bottom_menu_bar_new_profile_title)).setTypeface(null, Typeface.BOLD);
            try {
                mNewProfiles.setVisisble(true);
                mProfiles.setVisisble(false);
            }
            catch (Exception e){

            }
        }
        invalidateOptionsMenu(); // invalidate Menu to force reload on fragments.
                                // Fragments will reload menu if visibility is set to View.VISIBLE.
    }
}