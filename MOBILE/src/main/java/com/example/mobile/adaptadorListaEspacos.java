package com.example.mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class adaptadorListaEspacos extends ArrayAdapter  {

    List<Sala> listaEspacos;
    Context context;

    public adaptadorListaEspacos(@NonNull Context context, List<Sala> title) {
        super(context, R.layout.item_lista_espacos, title);

        this.listaEspacos = title;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate( R.layout.item_lista_espacos, parent,false);
        TextView textView = view.findViewById(R.id.txtNomeEspaco);
        TextView textViewParticipantes = view.findViewById(R.id.txtParticipantes);
        textView.setText(listaEspacos.get(position).getNome());
        int numero = (listaEspacos.get(position).getParticipantes() *  listaEspacos.get(position).getAlocacao()) / 100;
        textViewParticipantes.setText("O número maximo de participantes é " + String.valueOf(numero));

        return view;
    }

}
