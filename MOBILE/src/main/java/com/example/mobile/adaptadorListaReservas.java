package com.example.mobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
    List<Reservas> listaReservasAtuais;
    Context context;
    String Nome_sala=" ";

    public adaptadorListaReservas(@NonNull Context context, List<Reservas> title) {
        super(context, R.layout.item_lista_reservas_atuais, title);


        this.listaReservasAtuais = title;
        this.context = context;
    }
    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Variavel
        Integer id_sala =listaReservasAtuais.get(position).getIDSala();
        Integer id_reserva =listaReservasAtuais.get(position).getIDReserva();
        String nome_reuniao=listaReservasAtuais.get(position).getNomeReserva();
        String nome_sala=listaReservasAtuais.get(position).getNomeSala();
        LocalDate data=listaReservasAtuais.get(position).getDataReserva();
        LocalTime HoraInicio=listaReservasAtuais.get(position).getHoraInicio();
        LocalTime HoraFim=listaReservasAtuais.get(position).getHoraFim();
        //Widgets
        View view = LayoutInflater.from(context).inflate( R.layout.item_lista_reservas_atuais, parent,false);

        LinearLayout cor=view.findViewById(R.id.ln1);
        TextView txt_nomeReuniao = view.findViewById(R.id.txtNomeReunião);
        TextView txt_sala = view.findViewById(R.id.txtsala);
        TextView txt_data=view.findViewById(R.id.txtdata);
        TextView txt_horaInicio = view.findViewById(R.id.txt_horaInicio);
        TextView txt_horaFim = view.findViewById(R.id.txt_horaFim);
        TextView txt_decorrer= view.findViewById(R.id.txtdecorrer);

        txt_data.setText(data.toString());
        txt_horaInicio.setText(HoraInicio.toString());
        txt_horaFim.setText(HoraFim.toString());

        if(nome_reuniao.isEmpty()||nome_reuniao==""||nome_reuniao==" "||nome_reuniao.trim().equals("")||nome_reuniao==null){
            txt_nomeReuniao.setVisibility(View.GONE);
            txt_sala.setText(nome_sala);
            txt_sala.setTextSize(20);
            txt_sala.setTextColor(Color.parseColor("#114b82"));
        }else{
            txt_sala.setText(nome_sala);
            txt_nomeReuniao.setText(nome_reuniao);
        }

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate= formatter.format(date);
        String data_daReserva=data.toString();

        String time_i=HoraInicio.toString();
        String time_f=HoraFim.toString();
        LocalTime time=LocalTime.now();
        LocalTime horaI= LocalTime.parse(time_i);
        LocalTime horaF= LocalTime.parse(time_f);

        int valori=time.compareTo(horaI);
        int valorf=horaF.compareTo(time);


        //ImageView img_btn=view.findViewById(R.id.btn_DE);
        if(data_daReserva.equals(strDate)) {
            cor.setBackgroundColor(Color.parseColor("#49e01f"));
            if(valori==0||valori==1){
                if(valorf==0||valorf==1) {
                    txt_decorrer.setVisibility(TextView.VISIBLE);
                    //img_btn.setImageResource(R.drawable.ic_timer);

                }
                else{
                    txt_decorrer.setVisibility(TextView.GONE);
                    //img_btn.setImageResource(R.drawable.ic_lixo);
                }
            }
            else{
                txt_decorrer.setVisibility(TextView.GONE);
                //img_btn.setImageResource(R.drawable.ic_lixo);
            }
        }
        else{
            txt_decorrer.setVisibility(TextView.GONE);
            cor.setBackgroundColor(Color.parseColor("#4791ff"));
            //img_btn.setImageResource(R.drawable.ic_lixo);
        }

        return view;
    }
}
