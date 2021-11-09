package com.example.bluetoothcom.menu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bluetoothcom.R;
import com.example.bluetoothcom.ToolDropdownFragment;
import com.example.bluetoothcom.controls.SettingsMenuInterface;
import com.example.bluetoothcom.data.BlueProfile;
import com.example.bluetoothcom.fragments.Profile;

import java.util.ArrayList;

public class ProfileSettingsMenuFragment extends Fragment implements ToolDropdownFragment.OnDropdownItemItemChangeListener {

    private ProfileSettingsMenuViewModel mViewModel;

    private View mView;
    private View tempView;
    private SettingsMenuInterface mMenuInterfaceListener;
    private ToolDropdownFragment mDropDown;

    private int mMenuId = 0;

    private ArrayList<ToolDropdownFragment.DropdownItem> mListOfDevices = new ArrayList<>();
    private ArrayList<ToolDropdownFragment.DropdownItem> mListOfServices = new ArrayList<>();
    private ArrayList<ToolDropdownFragment.DropdownItem> mListOfCharacteristics = new ArrayList<>();
    private String menuActive = "";
    private String menuProfile = "Profile";
    private String menuDevice = "Device";
    private String menuService = "Service";
    private String menuCharacteristic = "Characteristic";
    private Bitmap mProfileImageBmp;

    private Profile mProfile;

    private ArrayList<ToolDropdownFragment.DropdownItem> mListOfProfiles = new ArrayList<>();

    public Bitmap getProfileImageBmp(){ return mProfileImageBmp;}
    public void setProfileImageBmp(Bitmap bmp){ this.mProfileImageBmp = bmp;}

    public void setProfile(BlueProfile blueProfile){
        Profile profile = new Profile();
        profile.setProfileName( blueProfile.getName());

        setProfileImageBmp(blueProfile.getProfileImageBitmap());

        ImageView imgView = mView.findViewById(R.id.profile_settings_menu_profile_image);
        imgView.setImageBitmap(getProfileImageBmp());


    }
    public void hideKeyboard(){

        InputMethodManager im = (InputMethodManager) mView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);// getParentFragment().getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(mView.getWindowToken(),0);
    }
    public void setProfile(Profile profile){
        this.mProfile = profile;

        mListOfDevices.clear();
        profile.getListOfDevices().forEach(device ->{
            addDevice(device);
        });
        mListOfServices.clear();
        profile.getListOfServices().forEach(service ->{
            addService(service);
        });

        mListOfCharacteristics.clear();
        profile.getListOfCharacteristics().forEach(characteristic -> {
            addCharacteristic(characteristic);
        });


    }
    public void setMenuInterfaceListener(SettingsMenuInterface listener){
        mMenuInterfaceListener = listener;
    }


    public void setProfileList(ArrayList<BlueProfile> listOfProfiles){
        //TODO: set list of available profiles from database.
        this.mListOfProfiles.clear();

        listOfProfiles.forEach(profile -> {
            this.mListOfProfiles.add(new ToolDropdownFragment.DropdownItem(profile.getName(),profile.getName(),profile.getName()));
        });

        //this.mListOfProfiles = listOfProfiles;
        if( mDropDown == null) return;//TODO: return if no dropdown.

        mDropDown.setItemList(mListOfProfiles,true);
    }

    public void addDevice(BluetoothDevice device){
        //TODO: add device
        String name = device.getName() == null ? "noname" : device.getName();

        mListOfDevices.add(new ToolDropdownFragment.DropdownItem(name,name + "\r\n" + device.getAddress() ,device.getAddress() ));
        if( mDropDown == null) return;//TODO: return if no dropdown.

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if( menuActive == menuDevice)
                    mDropDown.setItemList(mListOfDevices,true);
            }
        });
    }
    public void clearServices(){
        mListOfServices.clear();
    }
    public void addService(BluetoothGattService service){

        String id = service.getUuid().toString();
        mListOfServices.add(new ToolDropdownFragment.DropdownItem(id,id,id));
        if( mDropDown == null) return; //TODO: return if no dropdown.

        if(menuActive == menuService ) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDropDown.addItem(new ToolDropdownFragment.DropdownItem(id,id,id),true);
                }
            });

        }

    }

    public void clearCharacteristics(){
        mListOfCharacteristics.clear();
    }
    public void addCharacteristic(BluetoothGattCharacteristic characteristic){
        String id = characteristic.getUuid().toString();
        mListOfCharacteristics.add(new ToolDropdownFragment.DropdownItem(id,id ,id));
        if(mDropDown == null) return;//TODO: return if no dropdown.

        try {
            if (menuActive == menuCharacteristic)
                mDropDown.setItemList(mListOfCharacteristics, true);
        }catch (Exception ex){

        }
    }

    public static ProfileSettingsMenuFragment newInstance() {
        return new ProfileSettingsMenuFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = tempView; //TODO: save view for future reference.

        hideKeyboard();

        EditText fieldDevice = mView.findViewById(R.id.profile_settings_menu_profile_device_name);
        TextView labelDeviceID = mView.findViewById(R.id.profile_settings_menu_profile_device_uuid);
        if( mProfile.getDevice() != null) {
            fieldDevice.setText(mProfile.getDevice().getName());
            labelDeviceID.setText(mProfile.getDevice().getId());
        }

        EditText fieldService = mView.findViewById(R.id.profile_settings_menu_profile_service);
        if( mProfile.getService() != null)
            fieldService.setText(mProfile.getService().getName());

        EditText fieldCharacteristic = mView.findViewById(R.id.profile_settings_menu_profile_characteristic);
        if( mProfile.getCharacteristic() != null)
            fieldCharacteristic.setText(mProfile.getCharacteristic().getName());



        ((Button)mView.findViewById(R.id.profile_settings_menu_profile_name_button_dropdown)).setOnClickListener(new View.OnClickListener() {
            //TODO: onProfileDropdown menu.
            @Override
            public void onClick(View v) {

                openDropdownMenu(v,menuProfile,R.id.profile_settings_menu_profile_name,600);

                if( mDropDown != null)
                    // TODO: add list of profiles if menu created.
                    mDropDown.setItemList(mListOfProfiles,true);
            }
        });


        ((Button)mView.findViewById(R.id.profile_settings_menu_profile_device_name_button_dropdown)).setOnClickListener(new View.OnClickListener() {
            //TODO: onDeviceDropdown menu.
            @Override
            public void onClick(View v) {

                openDropdownMenu(v,menuDevice,R.id.profile_settings_menu_profile_device_name,620);
                if( mDropDown != null)
                    mDropDown.setItemList(mListOfDevices,true);
            }
        });
        ((Button)mView.findViewById(R.id.profile_settings_menu_profile_service_button_dropdown)).setOnClickListener(new View.OnClickListener() {
            //TODO: onServiceDropdown menu.
            // @Override
            public void onClick(View v) {
                openDropdownMenu(v,menuService,R.id.profile_settings_menu_profile_service,500);
                if( mDropDown != null) {
                    if (mListOfServices.size() != 0)
                        mDropDown.setItemList(mListOfServices,true);
                }

            }
        });

        ((Button)mView.findViewById(R.id.profile_settings_menu_profile_characteristic_button_dropdown)).setOnClickListener(new View.OnClickListener() {
            //TODO: onCharacteristicDropdown menu.
            @Override
            public void onClick(View v) {
                openDropdownMenu(v,menuCharacteristic, R.id.profile_settings_menu_profile_characteristic,400);
                if( mDropDown != null)
                    mDropDown.setItemList(mListOfCharacteristics,true);
            }
        });





        ((Button)mView.findViewById(R.id.profile_settings_menu_profile_button_ok)).setOnClickListener(new View.OnClickListener() {
            //TODO: Dismiss button clicked.
            @Override
            public void onClick(View v) {
                hideKeyboard();
                ValueAnimator vay = ValueAnimator.ofInt(960);

                vay.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if( mView == null ) return;
                        ViewGroup.LayoutParams lp = mView.getLayoutParams();
                        lp.height = 960 - (int) animation.getAnimatedValue();
                        mView.setLayoutParams(lp);

                    }
                });


                vay.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);


                        ((ViewGroup) mView.getParent()).removeView(mView);
                        mMenuInterfaceListener.onProfileSettingsMenuOkClicked();
                    }
                });

                vay.setDuration(100);
                vay.start();


            }
        });

        ((Button)mView.findViewById(R.id.profile_settings_menu_profile_button_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageIntentLauncher.launch(imageIntent);
            }

        });

    }

    public void setProfileImage(View view, Bitmap profileImage){
        ImageView imgView = view.findViewById(R.id.profile_settings_menu_profile_image);
        if( mMenuInterfaceListener != null && profileImage != null) {
            imgView.setImageBitmap(profileImage);
        }


    }
    public ActivityResultLauncher<Intent> imageIntentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>(){


                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() != -1) return ;

                    Bundle extras = result.getData().getExtras();
                    Bitmap bmp = (Bitmap) extras.get("data");



                    ImageView imgView = mView.findViewById(R.id.profile_settings_menu_profile_image);
                    imgView.setImageBitmap(bmp);
                    if( mMenuInterfaceListener != null)
                        mMenuInterfaceListener.onTakeImage(bmp);


                }
            }
    );

    // TODO: dropdown menu generic.
    private void openDropdownMenu(View v,String menuName, int fieldId, int maxHeight){
        hideKeyboard();

        if( mDropDown != null){ //TODO: cloe previous menu and fire onMenuClose event.
            if( menuActive != "") {
                mMenuInterfaceListener.onMenuClose(menuActive, "");// TODO: fire event.
            }

            ((ViewGroup)mView).removeView(mDropDown.getView());
            mDropDown = null;
            if( mMenuId == v.getId())
                return; // TODO: return if same menu... allows for menu retraction.
        }

        mDropDown = new ToolDropdownFragment();
        menuActive = menuName;
        mMenuId = v.getId();

        EditText editField = mView.findViewById(fieldId);

        View view  = mDropDown.onCreateView(getLayoutInflater(),(ViewGroup) mView,null);

        view.setLayoutParams( new LinearLayout.LayoutParams(0,0));
        mDropDown.setMenuHeight(maxHeight);
        mDropDown.setTextField(editField);

        ((ViewGroup)mView).addView(view,1);
        //TODO: allow menu to expand pass the parent.
        ((ViewGroup)mView).setClipToPadding(false);
//        ((ViewGroup)mView).setClipChildren(false); // TODO: set this on parent.

        mDropDown.setDropdownChangeListener(ProfileSettingsMenuFragment.this);
        mMenuInterfaceListener.onMenuOpen(menuActive,"n/a");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        tempView =  inflater.inflate(R.layout.profile_settings_menu_fragment, container, false);
        //TODO: save tempView onCreate, this function might be called multiple times nulling the view.
        return tempView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileSettingsMenuViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDropdownChange(int fieldId, ToolDropdownFragment.DropdownItem item) {

        mMenuInterfaceListener.onMenuClose(menuActive,"");

        switch (fieldId){
            case R.id.profile_settings_menu_profile_name:
                ((TextView)mView.findViewById(R.id.profile_settings_menu_profile_description)).setText(item.getId());

                mMenuInterfaceListener.onMenuChange( menuProfile,item);
                break;
            case R.id.profile_settings_menu_profile_device_name:

                ((TextView)mView.findViewById(R.id.profile_settings_menu_profile_device_uuid)).setText(item.getId());

                ((TextView)mView.findViewById(R.id.profile_settings_menu_profile_service)).setText("");
                ((TextView)mView.findViewById(R.id.profile_settings_menu_profile_characteristic)).setText("");

                mMenuInterfaceListener.onMenuChange(menuDevice,item);
                break;
            case R.id.profile_settings_menu_profile_service:
                mMenuInterfaceListener.onMenuChange(menuService,item);
                ((TextView)mView.findViewById(R.id.profile_settings_menu_profile_characteristic)).setText("");
                break;
            case R.id.profile_settings_menu_profile_characteristic:
                mMenuInterfaceListener.onMenuChange(menuCharacteristic,item);
                break;
        }
        if( mDropDown != null){
            ((ViewGroup)mView).removeView(mDropDown.getView());
            mDropDown = null;
            return;
        }

    }


}