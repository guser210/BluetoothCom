package com.example.bluetoothcom.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
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
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.bluetoothcom.R;
import com.example.bluetoothcom.ToolDropdownFragment;
import com.example.bluetoothcom.controls.ControlComm;
import com.example.bluetoothcom.controls.ControlConsoleFragment;
import com.example.bluetoothcom.controls.ControlEventInterface;
import com.example.bluetoothcom.controls.SettingsMenuInterface;
import com.example.bluetoothcom.data.BlueProfile;
import com.example.bluetoothcom.databinding.ActivityProfileBinding;
import com.example.bluetoothcom.fragments.NewProfileFragment;
import com.example.bluetoothcom.fragments.Profile;
import com.example.bluetoothcom.fragments.ProfileViewModel;
import com.example.bluetoothcom.menu.ProfileSettingsMenuFragment;

import java.util.List;
import java.util.Optional;

public class ProfileActivity extends AppCompatActivity implements ControlEventInterface, SettingsMenuInterface {
    private ProfileViewModel mViewModel;
    private ActivityProfileBinding binding;
    private View mView;
    private ProfileSettingsMenuFragment mSettingsMenu;
    private View settingsMenuView;
    private Menu mMenu;
    private boolean profileActive = true;
    BluetoothManager mBleManager = null;
    BluetoothAdapter mBleAdapter = null;
    BluetoothLeScanner mBleScanner = null;
    BluetoothGatt mBlueToothConnection = null;
    BluetoothGattCharacteristic mBluetoothCharacteristic = null;

    private Profile mProfile = new Profile();

    private String menuProfile = "Profile";
    private String menuDevice = "Device";
    private String menuService = "Service";
    private String menuCharacteristic = "Characteristic";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        profileActive = false;

       // mBleScanner.stopScan(scannerCallback);

        if( mBluetoothCharacteristic != null){
            mBlueToothConnection.setCharacteristicNotification(mBluetoothCharacteristic,false);
        }
      //  mBluetoothCharacteristic = null;
        try{
            mBlueToothConnection.disconnect();
            mBlueToothConnection.close();

        }catch (Exception ex){

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mView = binding.getRoot().getRootView();

        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        mViewModel.setView( mView,0);

        //String profileTitle = getIntent().getStringExtra("profileTitle");
        //setTitle(profileTitle); // Change title to current


        String profileName = getIntent().getStringExtra("profileName");

        mViewModel.loadProfileWith(profileName,getLayoutInflater());

        setTitle(profileName); // Change title to current

        mViewModel.getListOfCurrentControls().forEach(control ->{

            //TODO: set control settings.
            control.setEventListener(this);
            control.lockControl();
        });

        mBleManager =  getSystemService(BluetoothManager.class);

        if( mBleManager != null){
            mBleAdapter = mBleManager.getAdapter();
        }

        if( mBleAdapter != null && !mBleAdapter.isEnabled()){
            Intent bleIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(bleIntent);
            //startActivityForResult(bleIntent, 0);

        }


        mBleScanner = mBleAdapter.getBluetoothLeScanner();

        mBleScanner.startScan(scannerCallback);

    }

    private ScanCallback scannerCallback =  new ScanCallback() {
        //TODO:Bluetooth stuff.

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            //TODO:Bluetooth stuff.

            BluetoothDevice device = result.getDevice();


            if( !profileActive ) {

                try{
                    mBleScanner.stopScan(scannerCallback);
                    mBlueToothConnection.disconnect();
                    mBlueToothConnection.close();
                }catch (Exception ex){}
                return;
            }

            if( mViewModel.getProfile().getDeviceId() != null){
                if( device.getAddress().toString().equals(mViewModel.getProfile().getDeviceId())) {

                    //device.connectGatt();
                   mBleScanner.stopScan(scannerCallback);
                   mBlueToothConnection =  device.connectGatt(mView.getContext(), true,gattCallback);

                }


            }


            //TODO: remove duplicate devices, this list is utilized in connection settings.
            Optional<BluetoothDevice> duplicateDevice = mProfile.findDevice(device.getAddress().toString());
            if(duplicateDevice.isPresent())
                mProfile.getListOfDevices().remove(duplicateDevice.get());

            mProfile.addDevice(device);
            //mListOfBleDevices.add(device);

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
                mBleScanner.stopScan(scannerCallback);
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
                    // TODO: clear if cr on last character of line.
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
            List<BluetoothGattService> services  = gatt.getServices();

            if( mViewModel.getProfile().getServiceId() != null &&
                mViewModel.getProfile().getCharacteristicID() != null){
            //if( mProfile.getService() != null && mProfile.getCharacteristic() != null){
                services.forEach( service ->{
                    if( service.getUuid().toString().equals(mViewModel.getProfile().getServiceId())){
                        service.getCharacteristics().forEach(c -> {
                            if( c.getUuid().toString().equals(mViewModel.getProfile().getCharacteristicID())){
                                mBluetoothCharacteristic = c;
                                gatt.setCharacteristicNotification(c,true);
                               // mBleScanner.stopScan(scannerCallback);
                            }
                        });
                    }
                });
            }

            for( int i = 0; i < services.size(); i++){
                mSettingsMenu.addService(services.get(i));
            }

            gatt.getServices().forEach(service -> {
                mProfile.addService(service);
//                mListOfBleServices.add(service);
                mSettingsMenu.addService(service);
            });




        }
    };

    public void setControlData(String command){
        //TODO: pass data to controls.


        mViewModel.getListOfCurrentControls().forEach(c ->{

            runOnUiThread(new Runnable() {
                String cmd = command;
                @Override
                public void run() {
                    c.setData(cmd);
                }
            });

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_profile,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        //TODO: Menu events
        switch (item.getItemId()) {

            case R.id.menu_profile_save:
                //TODO: Save profile menu event.

                mViewModel.saveProfile(mViewModel.getProfile(),getSupportFragmentManager());

                return true;
            case R.id.menu_profile_delete_profile:
                mViewModel.deleteCurrentProfile();
                ViewGroup vg = (ViewGroup) mView;

                mViewModel.removeControlsFrom(vg);

                return true;
            case R.id.menu_profile_lock:
                MenuItem mi = mMenu.findItem(R.id.menu_profile_lock);
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

    private void disconnectFromDevice(){

        mBluetoothCharacteristic = null;
        try{
            mBlueToothConnection.disconnect();
            mBlueToothConnection.close();
            mBlueToothConnection = null;

        }catch (Exception ex){

        }
    }
    private void connectToDeviceWith( String address){

        Optional<BluetoothDevice> device = mProfile.findDevice(address);
        if( device.isPresent())
            mBlueToothConnection = device.get().connectGatt(this,true,gattCallback);



    }

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

                control.setEventListener(this);
            });
            BlueProfile profile = mViewModel.getProfile();

            profile.setDeviceName("");
            profile.setDeviceId("");
            profile.setServiceId("");
            profile.setCharacteristicID("");

            mSettingsMenu.setProfile(profile);


        }
        else if(menu.equals(menuDevice)) {
            //TODO: Device was selected.
            disconnectFromDevice();
            mSettingsMenu.clearServices();
            mSettingsMenu.clearCharacteristics();

            mProfile.setService(null);
            mProfile.setCharacteristic(null);
            mProfile.clearServices();

            mProfile.clearCharacteristics();;

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
                //mProfile.mService = new ToolDropdownFragment.DropdownItem(item.getId(),item.getId(),item.getId());
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

            }

        }
    }

    @Override
    public void onMenuOpen(String menu, String value) {

    }

    @Override
    public void onMenuClose(String menu, String value) {

    }

    @Override
    public void onStartScan() {

    }

    @Override
    public void onStopScan() {

    }

    @Override
    public void onConnectToDevice(ToolDropdownFragment.DropdownItem item) {

    }


    @Override
    public void onTakeImage(Bitmap profileImage) {

    }


    @Override
    public void onProfileSettingsMenuOkClicked() {
        EditText profileField = settingsMenuView.findViewById(R.id.profile_settings_menu_profile_name);

        mProfile.setProfileName(profileField.getText().toString());

        settingsMenuView = null;
        mSettingsMenu = null;
    }

}