package com.example.bluetoothcom.fragments;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.animation.ValueAnimator;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.bluetoothcom.controls.ControlOdometerV2Fragment;
import com.example.bluetoothcom.R;
import com.example.bluetoothcom.ToolDropdownFragment;
import com.example.bluetoothcom.controls.ControlComm;

import com.example.bluetoothcom.controls.ControlConsoleFragment;
import com.example.bluetoothcom.controls.ControlEventInterface;
import com.example.bluetoothcom.controls.ControlBigButtonFragment;
import com.example.bluetoothcom.controls.ControlStatusLabelFragment;
import com.example.bluetoothcom.controls.SettingsMenuInterface;
import com.example.bluetoothcom.data.BlueControl;
import com.example.bluetoothcom.data.BlueControlPropertyRepository;
import com.example.bluetoothcom.data.BlueControlRepository;
import com.example.bluetoothcom.data.BlueProfile;
import com.example.bluetoothcom.menu.ControlsMenuFragment;
import com.example.bluetoothcom.menu.ProfileSettingsMenuFragment;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewProfileFragment extends Fragment implements ControlEventInterface, SettingsMenuInterface {

    private int counter = 0;
    private ProfileViewModel mViewModel;
    private ControlsMenuFragment mControlsMenu;
    private View mView;
    private View tempView;
    private DragListener mDrop;
    private View controlsMenuView;
    private Menu mMenu;
    private boolean mScanning = false;

    private ProfileSettingsMenuFragment mSettingsMenu;
    private View settingsMenuView;

    BluetoothManager mBleManager = null;
    BluetoothAdapter mBleAdapter = null;
    BluetoothLeScanner mBleScanner = null;

    BluetoothGatt mBlueToothConnection = null;
    BluetoothGattCharacteristic mBluetoothCharacteristic = null;
    private String menuProfile = "Profile";
    private String menuDevice = "Device";
    private String menuService = "Service";
    private String menuCharacteristic = "Characteristic";

    private Profile mProfile = new Profile();



    private void disconnectFromDevice(){
        if( mBluetoothCharacteristic != null){
            mBlueToothConnection.setCharacteristicNotification(mBluetoothCharacteristic,false);
        }
       try{
           mBluetoothCharacteristic = null;
           mBlueToothConnection.disconnect();
           mBlueToothConnection.close();
           //mBlueToothConnection = null;

        }catch (Exception ex){

        }
    }
    private void connectToDeviceWith( String address){

        Optional<BluetoothDevice> device = mProfile.findDevice(address);
        if( device.isPresent())
            mBlueToothConnection = device.get().connectGatt(this.getContext(),true,gattCallback);
    }

    private ScanCallback scannerCallback =  new ScanCallback() {
        //TODO:Bluetooth stuff.

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            //TODO:Bluetooth stuff.

            BluetoothDevice device = result.getDevice();
            //TODO: remove duplicate devices, this list is utilized in connection settings.

            Optional<BluetoothDevice> duplicateDevice = mProfile.findDevice(device.getAddress().toString());
            if(duplicateDevice.isPresent())
                mProfile.mListOfDevices.remove(duplicateDevice.get());

            mProfile.addDevice(device);

            if( mSettingsMenu == null) return;
            try {
                mSettingsMenu.addDevice(device);
            }catch (Exception ex){

                //System.out.println(ex.getMessage());
                try{
                    mBleScanner.stopScan(scannerCallback);
                }catch (Exception ex2){

                }
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            //TODO:Bluetooth stuff.
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            //TODO:Bluetooth stuff.
        }
    };


    BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        //TODO:Bluetooth stuff.
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            //TODO:Bluetooth stuff.
            String name = gatt.getDevice().getName() == null ? gatt.getDevice().getAddress() : gatt.getDevice().getName();

            if( newState == BluetoothProfile.STATE_CONNECTED){
                setControlData("Connected to " + name);
                gatt.discoverServices();
            }else if( newState == BluetoothProfile.STATE_DISCONNECTED){
                setControlData("Disconnected from " + name);
            }
        }

        private String buffer = "";
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            //TODO:Bluetooth stuff.
            // Steps to capture data based on commands ending with \r
            // 1. append new value to last partial command buffer.
            // 2. split data based on \r
            // 3. get number of total commands - 1.
            // 4. set partial command as last command
            // 5. if last char on new data is \r reset number of commands to actual, clear buffer.
            // 6. cycle through each of the commands.

            String line = buffer + characteristic.getStringValue(0);
            buffer = "";
            int iline = line.indexOf("\r");
            if(iline >= 0){

                String []lines = line.split("\r");

                int imax = lines.length-1;
                buffer = lines[lines.length-1];
                if( line.charAt(line.length()-1) == '\r'){
                    // TODO: clear if CR on last character of line.
                    imax = lines.length;

                    buffer = "";
                }

                for( int i = 0; i < imax; i++){
                    //TODO: send incoming commands to available controls.
                    //TODO: each control should consume what it can and ignore data that does not apply.
                    setControlData(lines[i]);

                }

            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            //TODO:Bluetooth stuff.
            List<BluetoothGattService> services  = mBlueToothConnection.getServices();
            for( int i = 0; i < services.size(); i++){
                AtomicBoolean addService = new AtomicBoolean(false);
                addService.set(false);

                int index = i;
                services.get(i).getCharacteristics().forEach(c ->{
                    if(c.getWriteType() == 1){
                        addService.set(true);
                        }
                });

                if( addService.get()) {
                    mSettingsMenu.addService(services.get(index));
                    mProfile.addService(services.get(index));
                }

            }

            mBlueToothConnection.getServices().forEach(service -> {
                //mProfile.addService(service);

                //mSettingsMenu.addService(service);
            });




        }
    };
    @Override
    public void onMenuChange(String menu, ToolDropdownFragment.DropdownItem item) {
        // TODO: Settings menu interface method
        //System.out.println("Menu changed : " + menu);

        if( menu.equals(menuProfile)){
            //TODO: Profile selected.
            //
            ViewGroup vg = (ViewGroup)mView;
            mViewModel.removeControlsFrom(vg);
            //TODO: loadProfile
            mViewModel.loadProfileWith(item.getName(),getLayoutInflater());
            mViewModel.getListOfCurrentControls().forEach(control ->{
                //TODO: set control settings.

                control.setEventListener(NewProfileFragment.this);
            });
            BlueProfile profile = mViewModel.getProfile();

            profile.setDeviceName("");
            profile.setDeviceId("");
            profile.setServiceId("");
            profile.setCharacteristicID("");

//            mViewModel.setProfileImageFrom(profile);

            mSettingsMenu.setProfile(profile);

        }
        else if(menu.equals(menuDevice)) {
            //TODO: Device was selected.
            disconnectFromDevice();
            mSettingsMenu.clearServices();
            mSettingsMenu.clearCharacteristics();

            //mProfile.mService = null;
            //mProfile.mCharacteristic = null;
            mProfile.setService(null);
            mProfile.setCharacteristic(null);

            mProfile.clearServices();
            mProfile.clearCharacteristics();

            Optional<BluetoothDevice> device = mProfile.findDevice(item.getId());
            if( device.isPresent()) {

                mProfile.setDevice(new ToolDropdownFragment.DropdownItem(item.getName(), item.getDisplayName(), item.getId()));
            }

            connectToDeviceWith(item.getId());
        }
        else if( menu.equals(menuService)){
            //TODO: Service was selected.
            mSettingsMenu.clearCharacteristics();


            Optional<BluetoothGattCharacteristic> characteristic = mProfile.findCharacteristic(mProfile.getCharacteristic());
            if( characteristic.isPresent()) {

                if( mBlueToothConnection != null)
                    mBlueToothConnection.setCharacteristicNotification(characteristic.get(),false);
            }
            mProfile.clearCharacteristics();

            Optional<BluetoothGattService> service = mProfile.findService(item.getId());
            if( service.isPresent()){
                mProfile.setService(new ToolDropdownFragment.DropdownItem(item.getName(), item.getDisplayName(), item.getId()));
                service.get().getCharacteristics().forEach(c ->{

                    mProfile.addCharacteristic(c);
                    mSettingsMenu.addCharacteristic(c);
                });
            }

        }
        else if( menu.equals(menuCharacteristic)){
            //TODO: Characteristic was selected.

            Optional<BluetoothGattCharacteristic> characteristic = mProfile.findCharacteristic(item.getId());
            if( characteristic.isPresent()){
                mProfile.setCharacteristic(new ToolDropdownFragment.DropdownItem(item.getName(), item.getDisplayName(), item.getId()));
                mBlueToothConnection.setCharacteristicNotification(characteristic.get(),true);
                mBluetoothCharacteristic =  characteristic.get();

            }

        }

    }
    @Override
    public void onMenuOpen(String menu, String value) {
        // TODO: Settings menu interface method
        //System.out.println("Menu open: " + menu + " Value: " + value);
        if( menu.equals(menuService)){
        }
    }

    @Override
    public void onMenuClose(String menu, String value) {
        // TODO: Settings menu interface method
        //System.out.println("Menu close: " + menu + " Value: " + value);

    }

    @Override
    public void onStartScan() {
        // TODO: Settings menu interface method

    }

    @Override
    public void onStopScan() {
        // TODO: Settings menu interface method

    }

    @Override
    public void onConnectToDevice(ToolDropdownFragment.DropdownItem item) {
        // TODO: Settings menu interface method
        //System.out.println("Menu connect to device: " + item.getName() + " " + item.getId());


    }


    private boolean mVisible = false;

    public static NewProfileFragment newInstance() {
        return new NewProfileFragment();
    }

    public void setVisisble(boolean visible){
        //TODO: enable this for autoconnect to bluetooth device.
        if( visible) {

            try{
                mBlueToothConnection.connect();

            }catch (Exception ex){}
        }
        else
            mBlueToothConnection.disconnect();

    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(mView.getVisibility() != View.VISIBLE)
            return;


        inflater.inflate(R.menu.menu_new_profile,menu);
        mMenu = menu;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mView = tempView;

        ViewGroup viewGroup = (ViewGroup)mView;
        mControlsMenu = new ControlsMenuFragment();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        controlsMenuView = mControlsMenu.onCreateView(getLayoutInflater(),viewGroup,savedInstanceState);


        controlsMenuView.setLayoutParams(new LinearLayout.LayoutParams(metrics.widthPixels,(metrics.heightPixels / 3) * 2));

        controlsMenuView.setY( metrics.heightPixels - 280);

        viewGroup.addView(controlsMenuView);

        FragmentTransaction t = getParentFragmentManager().beginTransaction();
        t.add(mControlsMenu,"controls menu");
        t.commit();

        mDrop = new DragListener();
        mView.setOnDragListener(mDrop);

        mBleManager =  getActivity().getSystemService(BluetoothManager.class);

        if( mBleManager != null){
            mBleAdapter = mBleManager.getAdapter();
        }

        if( mBleAdapter != null && !mBleAdapter.isEnabled()){
            Intent bleIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(bleIntent, 0);

        }


        mBleScanner = mBleAdapter.getBluetoothLeScanner();

        //TODO: scan if connection definition valid.
        //mBleScanner.startScan(scannerCallback);

        // NOTE: Location access needs to be given to the app on the device, under settings/location.


    }


    private ScanCallback stopScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        tempView = inflater.inflate(R.layout.new_profile_fragment, container, false);

        return  tempView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


        // TODO: Use the ViewModel
        mViewModel.setView(mView,2);
        //TODO: get all profiles from repository.
        mViewModel.getListOfProfiles().observe(getActivity(), new Observer<List<BlueProfile>>() {
            @Override
            public void onChanged(List<BlueProfile> blueProfiles) {
                mProfile.mListOfProfiles.clear();

                blueProfiles.forEach(profile ->{
                    mProfile.addProfile(profile);


                });
                if(mSettingsMenu != null)
                    mSettingsMenu.setProfileList(mProfile.mListOfProfiles);
            }

        });


    }

    public void setControlData(String command){
        //TODO: pass data to controls.
        for (ControlComm c: mViewModel.getListOfCurrentControls()        ) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                        c.setData(command);
                    }
            });


        }
    }
    @Override
    public void controlDataToDevice(String command) {
        //TODO: send data to connected device

        for (ControlComm c: mViewModel.getListOfCurrentControls()) {
            if( c instanceof ControlConsoleFragment){
                //TODO: echo data on local console controls.
                if( c.isEcho())
                    c.setData(command);
            }
            try {
                if( mBlueToothConnection != null && mBluetoothCharacteristic != null) {
                    mBluetoothCharacteristic.setValue(command + "\r");
                    mBlueToothConnection.writeCharacteristic(mBluetoothCharacteristic);
                }
            }catch (Exception e){

            }
        }
    }

    @Override
    public void onDelete(ControlComm control) {
        ViewGroup vg = (ViewGroup) mView;
        mViewModel.deleteControl(control,vg);
    }

    protected class DragListener implements View.OnDragListener{
        private int counter = 0;
        @Override
        public boolean onDrag(View v, DragEvent event) {

            int action = event.getAction();

            switch (action){
                case DragEvent.ACTION_DRAG_LOCATION:
                    if( counter++ == 2 ) {
                        mControlsMenu.hideMenu(true);

                    }
                    return true;
                case DragEvent.ACTION_DROP:
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    View controlView;
                    BlueControlRepository controlRepository = new BlueControlRepository(getActivity().getApplication());
                    BlueControl blueControl = controlRepository.getDefaultControlBy(item.getText().toString());
                    BlueControlPropertyRepository propertyRepository = new BlueControlPropertyRepository(getActivity().getApplication());
                    ControlComm controlComm;
                    if( item.getText().equals("Console")){
                        //System.out.println("Console dropped");

                        ControlConsoleFragment control = new ControlConsoleFragment();
                        controlView = control.onCreateView(getLayoutInflater(),(ViewGroup) getView(),null);
                        controlComm = control;


                    }
                    else if( item.getText().equals("BigButton")){
                       //System.out.println("Big Label dropped");

                        ControlBigButtonFragment control = new ControlBigButtonFragment();
                        controlView = control.onCreateView(getLayoutInflater(),(ViewGroup) getView(),null);
                        controlComm = control;

                    }
                    else if( item.getText().equals("StatusLabel")){

                        ControlStatusLabelFragment control = new ControlStatusLabelFragment();
                        controlView = control.onCreateView(getLayoutInflater(),(ViewGroup) getView(),null);
                        controlComm = control;

                    }
                    else if( item.getText().equals("Odometer")){

                        ControlOdometerV2Fragment control = new ControlOdometerV2Fragment();
                        controlView = control.onCreateView(getLayoutInflater(),(ViewGroup) getView(),null);
                        controlComm = control;
                    }
                    else{
                        //controlView= new View(null);
                        return false;
                    }
                    //TODO: Event listener.
                    controlComm.setEventListener(NewProfileFragment.this);

                    controlView.setLayoutParams(new LinearLayout.LayoutParams((int) blueControl.getWidth(),(int)blueControl.getHeight()));

                    int width = blueControl.getWidth() == -2 ? 360 : (int) blueControl.getWidth();
                    int height = blueControl.getHeight() == -2 ? 100 : (int) blueControl.getHeight();
                    controlView.setX(event.getX() - (width/2));
                    controlView.setY(event.getY()-(height/2));

                    ViewGroup vg = (ViewGroup) mView;
                    vg.addView(controlView,vg.getChildCount()-1);


                    controlComm.setX( controlView.getX());
                    controlComm.setY( controlView.getY());
                    controlComm.setWidth(controlView.getLayoutParams().width);
                    controlComm.setHeight(controlView.getLayoutParams().height);

                    controlComm.setProperties( propertyRepository.getPropertiesForControlWith(blueControl.getId()) );

                    mViewModel.addControl(controlComm);

                    counter = 0;


                    return true;

            }

            return true;
        }
    }

    @Override
    public void onTakeImage(Bitmap profileImage) {
        mViewModel.setImage(profileImage);
    }

    @Override
    public void onProfileSettingsMenuOkClicked() {
        EditText profileField = settingsMenuView.findViewById(R.id.profile_settings_menu_profile_name);

        mProfile.setProfileName(profileField.getText().toString());


        settingsMenuView = null;
        mSettingsMenu = null;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        //TODO: Menu events
        switch (item.getItemId()) {
            case R.id.menu_new_profile_settings:
                //TODO: Settings Menu selected.
                if (mSettingsMenu != null) return false;


//                DialogFragment da = new ErrorMessage("Error please run");
//                da.show(getActivity().getSupportFragmentManager(), "help");

                mSettingsMenu = new ProfileSettingsMenuFragment();
                settingsMenuView = mSettingsMenu.onCreateView(getLayoutInflater(), (ViewGroup) mView, null);

                settingsMenuView.setLayoutParams(new LinearLayout.LayoutParams(800, 0));

                ValueAnimator vay = ValueAnimator.ofInt(1020);

                vay.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ViewGroup.LayoutParams lp = settingsMenuView.getLayoutParams();
                        lp.height = (int) animation.getAnimatedValue();
                        settingsMenuView.setLayoutParams(lp);
                    }
                });

                vay.setDuration(200);
                vay.start();

                settingsMenuView.setX(5);
                settingsMenuView.setY(190);

                ((ViewGroup) mView).addView(settingsMenuView);
                //TODO: allow menu to expand pass parent.
//                ((ViewGroup)mView).setClipToPadding(false);
                ((ViewGroup) mView).setClipChildren(false);

                FragmentTransaction t = getParentFragmentManager().beginTransaction();
                t.add(mSettingsMenu, "menuSettings");
                t.commit();

                mSettingsMenu.setProfile(mProfile);
                mSettingsMenu.setMenuInterfaceListener(NewProfileFragment.this);

                mSettingsMenu.setProfileList(mProfile.mListOfProfiles);


                mBleScanner.startScan(scannerCallback);

                EditText profileField = settingsMenuView.findViewById(R.id.profile_settings_menu_profile_name);
                if( mProfile.getProfileName() != null)
                    profileField.setText(mProfile.getProfileName());

//                if( mViewModel.getProfileImage() == null)
//                    mSettingsMenu.setProfileImage(settingsMenuView,mSettingsMenu.getProfileImageBmp());
//                else

                mSettingsMenu.setProfileImage(settingsMenuView,mViewModel.getProfileImage());


                return true;
            case R.id.menu_new_profile_clear:
                //TODO: Clear profile menu event.
                ViewGroup vg = (ViewGroup) mView;

                mViewModel.removeControlsFrom(vg);
                return true;
            case R.id.menu_new_profile_save:
                //TODO: Save profile menu event.


                if( mProfile.getProfileName() == null |
                        mProfile.getDevice() == null |
                        mProfile.getCharacteristic() == null
                ){  return true;}

                BlueProfile newProfile = new BlueProfile(0,mViewModel.getProfileNextSort(),mProfile.getProfileName(),"Test description");

                newProfile.setDeviceName(mProfile.getDevice().getName());

                //if( mProfile.mDevice != null)
                newProfile.setDeviceId(mProfile.getDevice().getId());
                //if( mProfile.mService != null)
                newProfile.setServiceId(mProfile.getService().getId());
                //if( mProfile.mCharacteristic != null)
                newProfile.setCharacteristicID(mProfile.getCharacteristic().getId());
                mViewModel.saveProfile(newProfile ,getActivity().getSupportFragmentManager());

                return true;
            case R.id.menu_new_profile_lock:
                MenuItem mi = mMenu.findItem(R.id.menu_new_profile_lock);
                if( mi.getTitle().equals("Lock")){
                    mi.setTitle("Unlock");
                    mViewModel.lockControls();
                }else{
                    mi.setTitle("Lock");
                    mViewModel.unlockControls();
                }

                return true;
        }


        return false;
    }

}

