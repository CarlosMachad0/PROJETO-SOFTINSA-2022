package com.example.tablet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class Definicoes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.definicoes)


        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener { view ->
            val intent = Intent (this, EscolherSala::class.java)
            startActivity(intent)
        }



        val imageButton5 = findViewById<ImageButton>(R.id.imageButton5)
        imageButton5.setOnClickListener { view ->
            val intent = Intent (this, inicio::class.java)
            startActivity(intent)
        }



    }
}