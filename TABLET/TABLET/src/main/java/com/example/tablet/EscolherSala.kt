package com.example.tablet

import android.content.Intent

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class EscolherSala : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.escolhersala)
        val pref = applicationContext.getSharedPreferences("MyPref", 0)
        var idCentro = pref.getInt("idCentro", 0)
        downloadTask()


        val button4 = findViewById<Button>(R.id.button4)
        button4.setOnClickListener { view ->
            val intent = Intent(this, inicio::class.java)
            startActivity(intent)

        }


        val imageButton5 = findViewById<ImageButton>(R.id.imageButton5)
        imageButton5.setOnClickListener { view ->
            val intent = Intent (this, inicio::class.java)
            startActivity(intent)
        }


    }


    private fun chamasalas(listaEspacos: ArrayList<espacosClasse>) {


        val pref = applicationContext.getSharedPreferences("MyPref", 0)
        var idCentro = pref.getInt("idCentro", 0)
        val spinner1 = findViewById<Spinner>(R.id.spinnerSalas)

        val urlSalas = "https://softinsa.herokuapp.com/salas/listSalas/".plus(idCentro.toString())
        Log.e(urlSalas, "adeus")

        val requestQueue2 = Volley.newRequestQueue(this)
        val request2 = JsonObjectRequest(Request.Method.GET, urlSalas, null, { response ->
            try {

                val jsonArray = response.getJSONArray("" + "data")
                Log.i("salas", jsonArray.toString())
                for (i in 0 until jsonArray.length()) {

                    Log.i("sala", jsonArray[i].toString())
                    val distr = jsonArray.getJSONObject(i)

                    listaEspacos.add(
                        espacosClasse(
                            distr.getString("id").toInt(),
                            distr.getString("Nome").toString(),
                            distr.getString("EstadoId").toInt(),
                            distr.getString("Motivo_Bloqueio").toString(),

                        )
                    )
                }
                val adapter =
                    spinneradapter2(this, android.R.layout.simple_spinner_item, listaEspacos)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                spinner1.adapter = adapter

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, { error -> error.printStackTrace() })
        requestQueue2?.add(request2)

    }


    private fun downloadTask() {


        val listaEspacos = ArrayList<espacosClasse>()
        val listaCentros = ArrayList<centrosClasse>()
        val spinner = findViewById<Spinner>(R.id.spinnerCentros)
        val pref = applicationContext.getSharedPreferences("MyPref", 0)


        var urlCentros = "https://softinsa.herokuapp.com/centros/list"
        val requestQueue1 = Volley.newRequestQueue(this)
        val request1 = JsonObjectRequest(Request.Method.GET, urlCentros, null, { response ->
            try {
                val jsonArray = response.getJSONArray("" + "data")
                for (i in 0 until jsonArray.length()) {
                    val distr = jsonArray.getJSONObject(i)
                    listaCentros.add(
                        centrosClasse(
                            distr.getString("id").toInt(),
                            distr.getString("Nome").toString()
                        )
                    )
                }
                val adapter =
                    spinneradapter(this, android.R.layout.simple_spinner_item, listaCentros)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                spinner.adapter = adapter
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, { error -> error.printStackTrace() })
        requestQueue1?.add(request1)







        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                listaEspacos.clear()

                val editor: SharedPreferences.Editor = pref.edit()

                editor.putInt("idCentro", listaCentros[position].IDCentro).apply()
                editor.putString("CentroNome", listaCentros[position].Nome).apply()
                val idCentro = pref.getInt("idCentro", 0)



                chamasalas(listaEspacos)


            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }



















        val spinner1 = findViewById<Spinner>(R.id.spinnerSalas)


        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {


                val editor: SharedPreferences.Editor = pref.edit()
                editor.putInt("IDEspaco", listaEspacos[position].IDEspaco).apply()
                editor.putString("SalaNome", listaEspacos[position].Nome).apply()
                editor.putInt("EstadoId", listaEspacos[position].EstadoId).apply()
                editor.putString("Motivo_Bloqueio", listaEspacos[position].Motivo_Bloqueio).apply()






                Log.e("sala", listaEspacos.toString())
                val IDEspaco = pref.getInt("IDEspaco", 0)


                val idCentro = pref.getInt("idCentro", 0)


            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


    }


}




