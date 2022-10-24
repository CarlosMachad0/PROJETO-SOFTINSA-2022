package com.example.mobile;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class adaptadorListaReservasPassadas extends ArrayAdapter {
    List<Reservas> listasReservasPassadas;
    Context context;

    public adaptadorListaReservasPassadas(@NonNull Context context , List<Reservas> titile){
        super(context, R.layout.item_lista_reservas_passadas,titile);

        this.listasReservasPassadas=titile;
        this.context=context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        //Variavel
        Integer id_sala =listasReservasPassadas.get(position).getIDSala();
        Integer id_reserva =listasReservasPassadas.get(position).getIDReserva();
        String nome_reuniao=listasReservasPassadas.get(position).getNomeReserva();
        String nome_sala=listasReservasPassadas.get(position).getNomeSala();
        LocalDate data=listasReservasPassadas.get(position).getDataReserva();
        LocalTime HoraInicio=listasReservasPassadas.get(position).getHoraInicio();
        LocalTime HoraFim=listasReservasPassadas.get(position).getHoraFim();
        //Widgets
        View view = LayoutInflater.from(context).inflate( R.layout.item_lista_reservas_passadas, parent,false);
        LinearLayout cor=view.findViewById(R.id.ln1);
        TextView txt_nomeReuniao = view.findViewById(R.id.txtNomeReunião);
        TextView txt_sala = view.findViewById(R.id.txtsala);
        TextView txt_data=view.findViewById(R.id.txtdata);
        TextView txt_horaInicio = view.findViewById(R.id.txt_horaInicio);
        TextView txt_horaFim = view.findViewById(R.id.txt_horaFim);

        if(nome_reuniao.isEmpty()||nome_reuniao==""||nome_reuniao==" "||nome_reuniao.trim().equals("")||nome_reuniao==null){
            txt_nomeReuniao.setVisibility(View.GONE);
            txt_sala.setText(nome_sala);
            txt_sala.setTextSize(20);
            txt_sala.setTextColor(Color.parseColor("#114b82"));
        }else{
            txt_nomeReuniao.setText(nome_reuniao);
            txt_sala.setText(nome_sala);
        }
        txt_sala.setText(nome_sala);
        txt_data.setText(data.toString());
        txt_nomeReuniao.setText(nome_reuniao);
        txt_horaInicio.setText(HoraInicio.toString());
        txt_horaFim.setText(HoraFim.toString());
        cor.setBackgroundColor(Color.parseColor("#114b82"));
        return view;
    }
}
