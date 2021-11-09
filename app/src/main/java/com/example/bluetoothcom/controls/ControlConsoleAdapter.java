package com.example.bluetoothcom.controls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothcom.R;

import java.util.LinkedList;
import java.util.List;


public class ControlConsoleAdapter extends RecyclerView.Adapter<ControlConsoleAdapter.ViewHolder> {
    private ControlConsoleAdapter adapter;
    private RecyclerView recyclerView;

    private List<ConsoleLine> listOflines ;
    private int lineCounter = 1;

    private int maxLines = 100;
    private float mTextSize = 14;
    public boolean mPaused = false;


    public void setTextSize(float textSize){
        this.mTextSize = textSize;
        notifyDataSetChanged();
    }
    public void setMaxLines(int maxLines){
        this.maxLines = maxLines;

        while(listOflines.size() > maxLines){
            listOflines.remove(0);
        }
        notifyDataSetChanged();

    }
    public void addLine( String text){

        if(mPaused) return;
        ConsoleLine consoleLine = new ConsoleLine(lineCounter++,text);
        listOflines.add(consoleLine);
        if( listOflines.size() > maxLines){

            listOflines.remove(0);
        }
        notifyDataSetChanged();
    }

    public ControlConsoleAdapter() {
        lineCounter = 1;
        listOflines = new LinkedList<ConsoleLine>();

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        LayoutInflater inflater =LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.control_console_cell,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return  viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String line = String.format("%3s",String.valueOf(listOflines.get(position).line));
        holder.line.setTextSize(mTextSize);
        holder.text.setTextSize(mTextSize);
        holder.line.setText( line);
        holder.text.setText(listOflines.get(position).text);
    }

    @Override
    public int getItemCount() {
        return listOflines.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView line;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            line = (TextView) itemView.findViewById(R.id.console_cell_line);

            text = (TextView) itemView.findViewById(R.id.console_cell_text);
        }
    }

    private class ConsoleLine{
        int line;
        String text;

        public ConsoleLine(int line, String text) {
            this.line = line;
            this.text = text;
        }
    }
}
