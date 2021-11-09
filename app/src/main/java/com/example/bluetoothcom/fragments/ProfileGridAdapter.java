package com.example.bluetoothcom.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bluetoothcom.R;
import com.example.bluetoothcom.data.BlueProfile;


import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ProfileGridAdapter extends BaseAdapter {

    ArrayList<BlueProfile> listOfProfiles;
    private Context context;
    private LayoutInflater inflater;


    public ProfileGridAdapter(Context context, ArrayList<BlueProfile> listOfProfiles) {
        this.listOfProfiles = listOfProfiles;
        this.context  = context;
        this.listOfProfiles = listOfProfiles;
    }

    public void setListOfProfiles(ArrayList<BlueProfile> listOfProfiles){
        this.listOfProfiles = listOfProfiles;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return listOfProfiles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        if( convertView == null){
            convertView = inflater.inflate(R.layout.profile_grid_cell,parent,false);

        }

        TextView profileName = convertView.findViewById(R.id.profile_name);
        profileName.setText(listOfProfiles.get(position).getName());



        ImageView image = convertView.findViewById(R.id.profile_image);
        ByteBuffer bf = listOfProfiles.get(position).getProfileImage();
        if( bf == null ) {
            image.setImageResource(R.drawable.thumb_odometer);
            return convertView;
        }
        bf.rewind();
        Bitmap bmp = Bitmap.createBitmap(listOfProfiles.get(position).getImageWidth(),
                listOfProfiles.get(position).getImageHeight(),
                Bitmap.Config.ARGB_8888);

        bmp.copyPixelsFromBuffer(bf);

        image.setImageBitmap(bmp);


        return convertView;
    }
}

