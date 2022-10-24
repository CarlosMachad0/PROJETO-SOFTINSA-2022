package com.example.tablet;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class adaptadorListaReservas extends ArrayAdapter {
    List<reunioes> listaReservasAtuais;
    Context context;
    String Nome_sala=" ";

    public adaptadorListaReservas(@NonNull Context context, List<reunioes> title) {
        super(context, R.layout.item, title);


        this.listaReservasAtuais = title;
        this.context = context;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Variavel


        String Unome=listaReservasAtuais.get(position).getUnome();
        String Pnome=listaReservasAtuais.get(position).getPnome();
        String data=listaReservasAtuais.get(position).getDataReserva();
        String HoraInicio=listaReservasAtuais.get(position).getHoraInicio();
        String HoraFim=listaReservasAtuais.get(position).getHoraFim();
        //Widgets


        View view = LayoutInflater.from(context).inflate( R.layout.item, parent,false);





        TextView txt_Pnome = view.findViewById(R.id.txt_Pnome);
        TextView txt_Unome = view.findViewById(R.id.txt_Unome);

        TextView txt_data=view.findViewById(R.id.txtdata);
        TextView txt_horaInicio = view.findViewById(R.id.txt_horaInicio);
        TextView txt_horaFim = view.findViewById(R.id.txt_horaFim);
        TextView txt_decorrer= view.findViewById(R.id.txtdecorrer);
        txt_data.setText(data.toString());
        txt_horaInicio.setText(HoraInicio.toString());
        txt_horaFim.setText(HoraFim.toString());
        txt_Pnome.setText(Pnome.toString());
        txt_Unome.setText(Unome.toString());







        return view;
    }



}



