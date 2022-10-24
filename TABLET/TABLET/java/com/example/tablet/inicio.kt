package com.example.tablet

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageButton
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject

class inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio)



        Handler().postDelayed({
            val i = Intent(this, inicio::class.java)
            startActivity(i)
        } , 40000)






















        var pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode






        val listaEspacos = ArrayList<espacosClasse>()

        var idCentro = pref.getInt("idCentro", 0)
        var sala = pref.getInt("IDEspaco", 0)
        var centro = pref.getInt("IDEspaco", 0)
        var NCentro = pref.getString("CentroNome", 0.toString())
        var Nsala = pref.getString("SalaNome", 0.toString())
        var salaEstado = pref.getInt("EstadoId", 0)

        var IDEspaco = pref.getInt("IDEspaco",0)



        val urlSalas = "https://softinsa.herokuapp.com/salas/get/".plus(IDEspaco.toString())
        Log.e(urlSalas, "adeus")

        val requestQueue2 = Volley.newRequestQueue(this)
        val request2 = JsonObjectRequest(Request.Method.GET, urlSalas, null, { response ->
            try {

                val jsonArray = response.getJSONObject( "data")
                Log.i("salas", jsonArray.toString())
                for (i in 0 until jsonArray.length()) {


                    val distr = jsonArray.getString("EstadoId")

                    pref.edit().putInt("EstadoId", distr.toInt()).apply()


                    val distrr = jsonArray.getString("Motivo_Bloqueio")

                    pref.edit().putString("Motivo_Bloqueio", distrr.toString()).apply()








                }


            } catch (e: JSONException) {
                e.printStackTrace()


            }





        }, { error -> error.printStackTrace() })
        requestQueue2?.add(request2)

















        if (sala == 0){
        startActivity(Intent(this, Login:: class.java)) }


        if (salaEstado == 2){
            startActivity(Intent(this, Erro:: class.java)) }





        val txtnomesala: TextView = findViewById(R.id.textView7)


        txtnomesala.text = Nsala




        var url = "https://softinsa.herokuapp.com/salas/codigo/$sala"
        val requestQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET, url, null, {
                response ->try {
            val jsonArray = response.getJSONArray("" +"data")
            Log.i("Array", jsonArray.toString())
            Log.i("Array", jsonArray.length().toString())
            for (i in 0 until jsonArray.length()) {
                val distr = jsonArray.getJSONObject(i)

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })
        requestQueue?.add(request)





        val request_sala =
            object : StringRequest(Method.GET, url, Response.Listener { response ->
                try {

                    val Codigo_Sala = JSONObject(response)
                    val Data_QRcode=Codigo_Sala.optString("data")

                    val QRcode=Data_QRcode.substringAfter("data:image/png;base64,")

                    val imageBytes=Base64.decode(QRcode,Base64.DEFAULT)
                    val decodedImage=BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)

                    //Aqui metes a tua imageview
                    val mImageView= findViewById<View>(R.id.mImageView) as ImageView
                    mImageView.setImageBitmap(decodedImage)



                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error -> error.printStackTrace() })
            {
            }
        requestQueue?.add(request_sala)





        val imageButton = findViewById<ImageButton>(R.id.imageButton)
        imageButton.setOnClickListener { view ->
            val intent = Intent (this, lista::class.java)
            startActivity(intent)


        }



    }



}