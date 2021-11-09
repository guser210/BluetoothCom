package com.example.bluetoothcom.controls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bluetoothcom.R;
import com.example.bluetoothcom.data.BlueControl;

import java.util.ArrayList;

public class ControlsMenuAdapter extends BaseAdapter {

    ArrayList<BlueControl> listOfControls;
    private Context context;
    private LayoutInflater inflater;

    public ControlsMenuAdapter(Context context, ArrayList<BlueControl> listOfControls){

        this.context = context;
        setListOfControls(listOfControls);

    }

    public  void setListOfControls(ArrayList<BlueControl> listOfControls){
        this.listOfControls = listOfControls;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return listOfControls.size();
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
        if(inflater == null){
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if( convertView == null){
            convertView = inflater.inflate(R.layout.controls_menu_cell,parent,false);
        }

        TextView controlTitle = convertView.findViewById(R.id.controls_menu_cell_title);
        controlTitle.setText(listOfControls.get(position).getName());
        ImageView img = convertView.findViewById(R.id.controls_menu_cell_image);
        img.setImageResource(listOfControls.get(position).getImageId());
      //  img.setTag(listOfControls.get(position).getImageId());
        return convertView;
    }


}
