package com.example.bluetoothcom.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.bluetoothcom.R;
import com.example.bluetoothcom.data.BlueProfile;
import com.example.bluetoothcom.main.ProfileActivity;

import java.util.ArrayList;
import java.util.List;


public class ProfilesFragment extends Fragment {

    private int counter = 0;
    private ProfilesViewModel mViewModel;

    private View tempView;

    private Button button;

    private boolean mVisible = false;
    public static ProfilesFragment newInstance() {
        return new ProfilesFragment();
    }
    public void setVisisble(boolean visible){
        this.mVisible = visible;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mViewModel = new ViewModelProvider(this).get(ProfilesViewModel.class);

        mViewModel.mView = tempView;


        mViewModel.mGridView = mViewModel.mView.findViewById(R.id.grid_profiles);

        mViewModel.mAdapterGrid = new ProfileGridAdapter(this.getContext(),new ArrayList<>());
        mViewModel.mGridView.setAdapter(mViewModel.mAdapterGrid);

        mViewModel.getProfiles().observe(this, new Observer<List<BlueProfile>>() {
            @Override
            public void onChanged(List<BlueProfile> blueProfiles) {
                mViewModel.setListOfProfiles((ArrayList<BlueProfile>) blueProfiles);
            }
        });

        mViewModel.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView profileName = view.findViewById(R.id.profile_name);

                Intent profileIntent = new Intent(view.getContext(), ProfileActivity.class);
                profileIntent.putExtra("profileName" ,profileName.getText());
                startActivity(profileIntent);

            }
        });


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(mViewModel.mView.getVisibility() == View.VISIBLE)
            inflater.inflate(R.menu.menu_profiles,menu);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        tempView =  inflater.inflate(R.layout.profiles_fragment, container, false);


        return tempView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(ProfilesViewModel.class);
//        // TODO: Use the ViewModel
//


    }

}