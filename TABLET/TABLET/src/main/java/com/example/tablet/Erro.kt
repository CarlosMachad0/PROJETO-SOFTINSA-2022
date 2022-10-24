package com.example.tablet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class Erro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.erro)


        val imageButton2 = findViewById<ImageButton>(R.id.imageButton2)
        imageButton2.setOnClickListener { view ->
            val intent = Intent (this, Login::class.java)
            startActivity(intent)
        }



        var pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        var editor = pref.edit()
        var sala = pref.getInt("Idsala", 0)
        var centro = pref.getInt("Idsala", 0)
        var Nsala = pref.getString("SalaNome", 0.toString())
        var Motivo = pref.getString("Motivo_Bloqueio", 0.toString())

        val TxtMotivo: TextView = findViewById(R.id.textView4)
        TxtMotivo.text = Motivo


        val TxtNsala: TextView = findViewById(R.id.textView9)

        TxtNsala.text = Nsala


    }



}

