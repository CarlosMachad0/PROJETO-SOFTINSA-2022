package com.example.tablet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class lista : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lista)
        var pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode

        val array_list = ArrayList<reunioes>()
        var Nsala = pref.getString("SalaNome", 0.toString())
        val IDEspaco = pref.getInt("IDEspaco", 0)

        val url= "https://softinsa.herokuapp.com/reservas/listReservasSala/".plus(IDEspaco.toString())







        val requestQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET, url, null, {
                response ->try {

            val jsonArray = response.getJSONArray("" + "data")
            Log.i("Array", jsonArray.toString())
            Log.i("Array", jsonArray.length().toString())


            for (i in 0 until jsonArray.length()) {
                val distr = jsonArray.getJSONObject(i)
                array_list.add(

                    reunioes(

                        distr.optString("DataReserva").toString(),
                        distr.optString("HoraInicio").toString(),
                        distr.optString("HoraFim").toString(),
                        distr.optString("Pnome").toString(),
                        distr.optString("Unome").toString()



                    )
                )
            }

            val arrayAdapter = adaptadorListaReservas( this, array_list)
            findViewById<ListView>(R.id.lista).adapter = arrayAdapter






        } catch (e: JSONException) {
            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })
        requestQueue?.add(request)





        val imageButton2 = findViewById<ImageButton>(R.id.imageButton2)
        imageButton2.setOnClickListener { view ->
            val intent = Intent (this, Login::class.java)
            startActivity(intent)
        }




        val imageButton4 = findViewById<ImageButton>(R.id.imageButton4)
        imageButton4.setOnClickListener { view ->
            val intent = Intent (this, inicio::class.java)
            startActivity(intent)
        }










        val txtnomesala: TextView = findViewById(R.id.textView6)


        txtnomesala.text = "Reservas " + Nsala





    }




}