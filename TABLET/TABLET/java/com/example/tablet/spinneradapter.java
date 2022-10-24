
package com.example.tablet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;



public class spinneradapter extends ArrayAdapter<centrosClasse>{

    // Your sent context
    private Context context;
    private  List<centrosClasse> values;

    public spinneradapter(Context context, int textViewResourceId, List<centrosClasse> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label1 = (TextView) super.getDropDownView(position, convertView, parent);
        label1.setTextColor(Color.BLACK);
        label1.setText(values.get(position).getNome());

        return label1;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getNome());

        return label;
    }
}




