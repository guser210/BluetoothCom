package com.example.bluetoothcom.fragments;

import android.app.Application;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bluetoothcom.controls.ControlOdometerV2Fragment;
import com.example.bluetoothcom.ErrorMessage;
import com.example.bluetoothcom.controls.ControlComm;
import com.example.bluetoothcom.controls.ControlConsoleFragment;
import com.example.bluetoothcom.controls.ControlBigButtonFragment;
import com.example.bluetoothcom.controls.ControlStatusLabelFragment;
import com.example.bluetoothcom.data.BlueControl;
import com.example.bluetoothcom.data.BlueControlProperty;
import com.example.bluetoothcom.data.BlueControlPropertyRepository;
import com.example.bluetoothcom.data.BlueControlRepository;
import com.example.bluetoothcom.data.BlueProfile;
import com.example.bluetoothcom.data.BlueProfileRepository;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private ArrayList<ControlComm> mListOfControls;
    private BlueProfile mProfile;
    private BlueProfileRepository mProfileRepo;
    private BlueControlRepository mControlRepo;
    private BlueControlPropertyRepository mPropertyRepo;
    private BlueProfile defaultProfile;

    private ByteBuffer mProfileImage;
    private int mImageWidth;
    private int mImageHeight;

    private int zOrder = 0;

    private List<BlueProfile> listOfProfiles;
    private View mView;
    private ViewGroup mViewGroup;

    public void deleteControl(ControlComm control,ViewGroup vg){
        try{
            vg.removeView(control.mCommView);
            getListOfCurrentControls().remove(control);
        }catch (Exception ex){}
    }
    public void setImage(Bitmap profileImage){
        this.mProfileImage = null;

        mImageWidth = 0;
        mImageHeight = 0;
        if( profileImage != null) {
            mImageWidth = profileImage.getWidth();
            mImageHeight = profileImage.getHeight();
            int imageSize = profileImage.getByteCount();
            this.mProfileImage = ByteBuffer.allocate(imageSize);

            profileImage.copyPixelsToBuffer(this.mProfileImage);
        }

        if( mProfile != null)
            mProfile.setProfileImage(this.mProfileImage);
    }

    public Bitmap getProfileImage(){
        if( this.mProfileImage == null) return null;

        Bitmap image = Bitmap.createBitmap (mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);

        this.mProfileImage.rewind();
        image.copyPixelsFromBuffer(this.mProfileImage);
        return image;




    }
    public void lockControls(){
        mListOfControls.forEach(control ->{
            control.lockControl();
        });
    }
    public void unlockControls(){
        mListOfControls.forEach(control ->{
            control.unlockControl();
        });
    }
    public void setView(View view,int zOrder ){
        this.zOrder = zOrder;
        this.mView = view;
        this.mViewGroup = (ViewGroup) this.mView;
    }

    public BlueProfile getProfile(){ return mProfile;};
    public ProfileViewModel(@NonNull Application application) {
        super(application);
        mProfileRepo = new BlueProfileRepository(application);
        mControlRepo = new BlueControlRepository(application);
        mPropertyRepo = new BlueControlPropertyRepository(application);



        mListOfControls = new ArrayList<ControlComm>();

    }

    public void removeControlAt(int index) {
        this.mListOfControls.remove(index);
    }

    public void addControl(ControlComm control){
        mListOfControls.add(control);
    }

    public void removeControlsFrom(ViewGroup vg) {

        while (getListOfCurrentControls().size() > 0) {

            vg.removeView(getListOfCurrentControls().get(0).mCommView);
            getListOfCurrentControls().get(0).mCommView = null;

            removeControlAt(0);
        }
        System.gc();
    }


    public ArrayList<ControlComm> getListOfCurrentControls(){
        return mListOfControls;
    }


    public void loadProfileWith(String profileName, LayoutInflater inflater){
        mListOfControls.clear();
        mProfile = getProfileWith(profileName);
        if( mProfile == null ) return;

        setImage(mProfile.getProfileImageBitmap());

        mListOfControls.clear();

        ArrayList<BlueControl> listOfControls = mControlRepo.getControlsFromProfileBy(mProfile.getId());
        listOfControls.forEach(control ->{
            loadControl(control,inflater);
        });



    }
    public void loadControl(BlueControl control, LayoutInflater inflater){
        View controlView = null;
        ControlComm genericControl = null;
        if(control.getName().equals("Console")){
            genericControl = new ControlConsoleFragment();
        }
        else if( control.getName().equals("BigButton")){
            //ControlsBigButtonFragment controlFragment = new ControlsBigButtonFragment();
            genericControl = new ControlBigButtonFragment();
        }else if( control.getName().equals("StatusLabel")){
            genericControl = new ControlStatusLabelFragment();
        }
        else if( control.getName().equals("Odometer")){
            genericControl = new ControlOdometerV2Fragment();
        }

        if(genericControl == null) return;


        controlView = genericControl.onCreateView(inflater,mViewGroup,null);

        controlView.setLayoutParams(new LinearLayout.LayoutParams((int)control.getWidth(),(int)control.getHeight()));

        //TODO: controls need to implement all functions of ControlComm.
        mViewGroup.addView(controlView, mViewGroup.getChildCount() - zOrder ); // TODO: might need count - 1 z-order
        controlView.setX(control.getX());
        controlView.setY(control.getY());



        genericControl.setProperties( mPropertyRepo.getPropertiesForControlWith(control.getId()));
        addControl(genericControl);
        genericControl.setX(control.getX());
        genericControl.setY( control.getY());
        genericControl.setWidth(control.getWidth());
        genericControl.setHeight( control.getHeight());

    }
    public void saveProfile(BlueProfile profile, FragmentManager manager){
        //TODO: Save profile

        //TODO: get profile.
        BlueProfile existingProfile = getProfileWith(profile.getName());
        if( existingProfile != null) {
            profile.setImageHeight(existingProfile.getImageHeight());
            profile.setImageWidth(existingProfile.getImageWidth());
            profile.setProfileImage(existingProfile.getProfileImage());
        }

        if(existingProfile != null) {
            //TODO: get controls.
            ArrayList<BlueControl> controlsToDelete = getControlsForProfileBy(existingProfile.getId());


            if( controlsToDelete != null){
                //TODO: delete properties for each control of existing profile.
                controlsToDelete.forEach(control ->{
                    mPropertyRepo.deletePropertiesForControlBy(control.getId());
                });
            }

            //TODO: 2. delete all Controls for existing profile.
            mControlRepo.deleteControlsFromProfileBy(existingProfile.getId());
            mControlRepo.deleteControlsFromProfileBy(existingProfile.getName());

            //TODO: delete existing profile.
            mProfileRepo.deleteProfile(existingProfile);

        }

        //TODO: this should return null once the profile is removed from database.
        BlueProfile newProfile = getProfileWith(profile.getName());
        while(newProfile != null) {
            //TODO: wait for existing profile to be deleted.
            newProfile = getProfileWith(profile.getName());
        }


        if( mProfileImage != null){
        profile.setProfileImage(mProfileImage);
        profile.setImageWidth(mImageWidth);
        profile.setImageHeight(mImageHeight);
        }

        mProfileRepo.insertProfile(profile);

        while( newProfile == null) {
            //TODO: waiti for profile to be inserted.
            newProfile = getProfileWith(profile.getName());
        }

        int controlId = mControlRepo.getMaxControlId();

        for( int indexControl = 0; indexControl < mListOfControls.size(); indexControl++) {
            controlId += 1;
            while( controlId <= 0){
                //TODO: controlID cannot be zero, zero implies the system will autogenerate the id.
                controlId += 1;
            }

            ControlComm c = mListOfControls.get(indexControl);
            int sort = indexControl;
            BlueControl control = new BlueControl(0, newProfile.getId(), sort, c.getControlName(), "", 0);

            control.setX(c.getX());
            control.setY(c.getY());

            control.setWidth(c.getWidth());
            control.setHeight(c.getHeight());

            control.setId(controlId);

            control.setProfileName(newProfile.getName());
            mControlRepo.insertControl(control);

            for( int indexProperty = 0; indexProperty < c.mCommListOfProperties.size(); indexProperty++){
                BlueControlProperty property = c.mCommListOfProperties.get(indexProperty);

                property.setControlID(controlId);
                property.setId(0);
                property.setSort(indexProperty);

                mPropertyRepo.insertProperty(property);

            }



        }


        DialogFragment confirmDialog = new ErrorMessage("Profile Saved","Profile " + profile.getName() + " Saved");

        confirmDialog.show(manager,"Saved");
    }

    public void deleteCurrentProfile(){
        if( mProfile == null) return;
        mListOfControls.forEach(control ->{
            mPropertyRepo.deletePropertiesForControlBy(control.getId());
        });
        mControlRepo.deleteControlsFromProfileBy(mProfile.getName());
        mProfileRepo.deleteProfileWith(mProfile.getName());
    }

    public LiveData<List<BlueProfile>> getListOfProfiles(){
        return mProfileRepo.getProfiles();
    }

    public BlueProfile getProfileWith(String profileName){
        return mProfileRepo.getProfileWith(profileName);
    }
    public int getProfileNextSort(){
        return mProfileRepo.getProfileNextSort();
    }


    public ArrayList<BlueControl> getControlsForProfileBy(String profileName){
        return mControlRepo.getControlsFromProfileBy(profileName);
    }
    public ArrayList<BlueControl> getControlsForProfileBy(int profileId){
        return mControlRepo.getControlsFromProfileBy(profileId);
    }

    public BlueProfile getDefaultProfile(){
        return mProfileRepo.getDefaultProfile();
    }

}