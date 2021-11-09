package com.example.bluetoothcom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothcom.fragments.NewProfileFragment;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ToolDropdownAdapter  extends RecyclerView.Adapter<ToolDropdownAdapter.ViewHolder> {

    private ArrayList<ToolDropdownFragment.DropdownItem> mListOfItems = new ArrayList<>();
    private OnDropdownItemClickListener listener;

    public interface OnDropdownItemClickListener {
        void onDropdownClick(ToolDropdownFragment.DropdownItem item);
    }

    public void setListOfItems(ArrayList<ToolDropdownFragment.DropdownItem> listOfItems, boolean removeDuplicates){

        this.mListOfItems.clear();
        for (ToolDropdownFragment.DropdownItem item : listOfItems) {
            addItem(item,removeDuplicates); // this ensures unique items.
        }

        notifyDataSetChanged();
    }
    public void addItem(ToolDropdownFragment.DropdownItem item, boolean removeDuplicates){

        // only add new items and update name for existing ones.
        if( !removeDuplicates)
            mListOfItems.add(item);
        else {
            ArrayList<ToolDropdownFragment.DropdownItem> dups = (ArrayList<ToolDropdownFragment.DropdownItem>) mListOfItems.stream().filter(d -> d.id.equals(item.id)).collect(Collectors.toList());
            if (dups.size() == 0) {
                mListOfItems.add(item);
            } else {
                dups.get(0).name = item.name;
            }
        }
            notifyDataSetChanged();

    }


    public ToolDropdownAdapter(OnDropdownItemClickListener listener ) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tool_dropdown_cell,parent,false);


        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       holder.item.setText(mListOfItems.get(position).getDisplayName());
       if( mListOfItems.get(position).getName().equals("noname"))
           holder.item.setTextColor(0xff000000);
       else
           holder.item.setTextColor(0xff0000ff);

        ToolDropdownFragment.DropdownItem item = mListOfItems.get(position);


       holder.item.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               listener.onDropdownClick(item);
           }
       });

    }

    @Override
    public int getItemCount() {
        return mListOfItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        Button item;
        int position = -1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.tool_dropdown_cell_button);

        }
    }



}


