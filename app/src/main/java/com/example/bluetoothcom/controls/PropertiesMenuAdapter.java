package com.example.bluetoothcom.controls;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothcom.R;
import com.example.bluetoothcom.data.BlueControlProperty;

import java.util.List;

public class PropertiesMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BlueControlProperty> mListOfProperties;


    public List<BlueControlProperty> getListOfProperties() {
        return mListOfProperties;
    }

    public PropertiesMenuAdapter(List<BlueControlProperty> listOfProperties){
        setListOfProperties(listOfProperties);
    }

    public void setListOfProperties(List<BlueControlProperty> listOfProperties){
        this.mListOfProperties = listOfProperties;
    }

    @Override
    public int getItemViewType(int position) {
        return mListOfProperties.get(position).getPropertyType();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cellView;
        RecyclerView.ViewHolder cellViewHolder;
        switch (viewType) {
            case 4:
                cellView = inflater.inflate(R.layout.properties_menu_cell_4, parent, false);
                cellViewHolder = new ViewHolder_4(cellView);
                break;
            case 3:
                cellView = inflater.inflate(R.layout.properties_menu_cell_3, parent, false);
                cellViewHolder = new ViewHolder_3(cellView);
                break;
            case 2:
                cellView = inflater.inflate(R.layout.properties_menu_cell_2, parent, false);
                cellViewHolder = new ViewHolder_2(cellView);
                break;
            default:
                cellView = inflater.inflate(R.layout.properties_menu_cell_1, parent, false);
                cellViewHolder = new ViewHolder_1(cellView);
                break;

        }
        return cellViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 1:
                ViewHolder_1 v1 = (ViewHolder_1) holder;
                v1.label.setText(mListOfProperties.get(position).getLabel());
                v1.value.setText(mListOfProperties.get(position).getValue());

                v1.value.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        getListOfProperties().get(v1.getAdapterPosition()).setValue(String.valueOf(s));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });
                break;
            case 2:
                ViewHolder_2 v2 = (ViewHolder_2) holder;
                v2.delimiterValue.setText( mListOfProperties.get(position).getDelimiterValue() );
                if( mListOfProperties.get(position).getDelimiterType() == 1)
                    v2.radioButtonCharacter.setChecked(true);
                else
                    v2.radioButtonValue.setChecked(true);

                v2.radioButtonCharacter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if( isChecked)
                            mListOfProperties.get(v2.getAdapterPosition()).setDelimiterType(1);
                        else
                            mListOfProperties.get(v2.getAdapterPosition()).setDelimiterType(0);
                    }
                });

                break;

            case 3:
                ViewHolder_3 v3 = (ViewHolder_3) holder;
                v3.fieldLabel.setText( mListOfProperties.get(position).getLabel());
                v3.fieldValue.setText(mListOfProperties.get(position).getValue());

                v3.defaultLabel.setText(mListOfProperties.get(position).getDefaultLabel());
                v3.defaultValue.setText(mListOfProperties.get(position).getDefaultValue());

                v3.delimiterValue.setText(mListOfProperties.get(position).getDelimiterValue());





                if( mListOfProperties.get(position).getDelimiterType() == 1) {
                    v3.radioButtonCharacter.setChecked(true);
                }
                else{
                    v3.radioButtonValue.setChecked(true);

                }

                v3.delimiterValue.setText(mListOfProperties.get(position).getDelimiterValue());



                v3.radioButtonValue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String value = v3.delimiterValue.getText().toString();
                        if( isChecked) {
                            v3.delimiterValue.setText(String.valueOf((int) value.toCharArray()[0]));
                        }else{
                            v3.delimiterValue.setText(String.valueOf((char)Integer.valueOf(value).intValue()));
                        }
                    }
                });

                v3.radioButtonCharacter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String value = v3.delimiterValue.getText().toString();
                        if( isChecked == false) {
                            v3.delimiterValue.setText(String.valueOf((int) value.toCharArray()[0]));
                        }else{
                            v3.delimiterValue.setText(String.valueOf((char)Integer.valueOf(value).intValue()));
                        }
                    }
                });


                v3.fieldValue.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        getListOfProperties().get(v3.getAdapterPosition()).setValue(String.valueOf(s));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });

                v3.delimiterValue.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        getListOfProperties().get(v3.getAdapterPosition()).setDelimiterValue(String.valueOf(s));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });

                v3.defaultValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getListOfProperties().get(v3.getAdapterPosition()).setDefaultValue(String.valueOf(s));
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

                v3.radioButtonCharacter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if( isChecked)
                            mListOfProperties.get(v3.getAdapterPosition()).setDelimiterType(1);
                        else
                            mListOfProperties.get(v3.getAdapterPosition()).setDelimiterType(0);
                    }
                });

            break;
            case 4:
                ViewHolder_4 v4 = (ViewHolder_4) holder;
                v4.fieldLabel.setText( mListOfProperties.get(position).getLabel());

                if( mListOfProperties.get(position).getValueInt() == 1)
                    v4.radioButtonYes.setChecked(true);
                else
                    v4.radioButtonNo.setChecked(true);

                v4.radioButtonYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if( isChecked)
                            mListOfProperties.get(v4.getAdapterPosition()).setValue("1");
                        else
                            mListOfProperties.get(v4.getAdapterPosition()).setValue("0");
                    }
                });

                break;
        }
    }



    @Override
    public int getItemCount() {
        return mListOfProperties.size();
    }


    public static class ViewHolder_1 extends RecyclerView.ViewHolder{
        TextView label;
        EditText value;

        public ViewHolder_1(@NonNull View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.properties_menu_cell_1_label);
            value = itemView.findViewById(R.id.properties_menu_cell_1_value);

        }
    }


    public static class ViewHolder_2 extends RecyclerView.ViewHolder{

        EditText delimiterValue;
        RadioButton radioButtonValue;
        RadioButton radioButtonCharacter;
        public ViewHolder_2(@NonNull View itemView) {
            super(itemView);

            delimiterValue = itemView.findViewById(R.id.properties_menu_cell_2_delimiter_value);
            radioButtonValue = itemView.findViewById(R.id.properties_menu_cell_2_option_type_value);
            radioButtonCharacter = itemView.findViewById(R.id.properties_menu_cell_2_option_type_character);
        }
    }

    public static class ViewHolder_3 extends RecyclerView.ViewHolder{

        TextView fieldLabel;
        EditText fieldValue;
        EditText delimiterValue;
        RadioButton radioButtonValue;
        RadioButton radioButtonCharacter;
        TextView defaultLabel;
        EditText defaultValue;
        public ViewHolder_3(@NonNull View itemView) {
            super(itemView);


            fieldLabel = itemView.findViewById(R.id.properties_menu_cell_3_field_label);
            fieldValue = itemView.findViewById(R.id.properties_menu_cell_3_field_value);
            delimiterValue = itemView.findViewById(R.id.properties_menu_cell_3_delimiter_value);
            radioButtonValue = itemView.findViewById(R.id.properties_menu_cell_3_option_type_value);
            radioButtonCharacter = itemView.findViewById(R.id.properties_menu_cell_3_option_type_character);
            defaultLabel = itemView.findViewById(R.id.properties_menu_cell_3_default_label);
            defaultValue = itemView.findViewById(R.id.properties_menu_cell_3_default_value);
        }
    }

    public static class ViewHolder_4 extends RecyclerView.ViewHolder{

        TextView fieldLabel;
        RadioButton radioButtonYes;
        RadioButton radioButtonNo;



        public ViewHolder_4(@NonNull View itemView) {
            super(itemView);


            fieldLabel = itemView.findViewById(R.id.properties_menu_cell_4_name);
            radioButtonYes = itemView.findViewById(R.id.properties_menu_cell_4_option_type_yes);
            radioButtonNo = itemView.findViewById(R.id.properties_menu_cell_4_option_type_no);


        }
    }



}
