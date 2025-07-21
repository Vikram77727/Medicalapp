package com.example.medicalapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MedicineAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> medicines;

    public MedicineAdapter(Context context, ArrayList<String> medicines) {
        super(context, 0, medicines);
        this.context = context;
        this.medicines = medicines;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView medicineText = convertView.findViewById(android.R.id.text1);
        medicineText.setText(medicines.get(position));

        return convertView;
    }
}
