package com.example.tablet

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

data class espacosClasse(
    var IDEspaco: Int,

    var Nome: String,

    var EstadoId: Int,

    var Motivo_Bloqueio: String


    )


fun listarEspacos(idCentro : Int, context : Context, listaEspacos : ArrayList<espacosClasse>)
{
    var urlEspacos = "https://softinsa.herokuapp.com/salas/listSalas/".plus(idCentro.toString())

    val requestQueue = Volley.newRequestQueue(context)
    val request = JsonObjectRequest(Request.Method.GET, urlEspacos, null, {
            response ->try {
        val jsonArray = response.getJSONArray("" + "data")
        for (i in 0 until jsonArray.length()) {
            val distr = jsonArray.getJSONObject(i)
            listaEspacos.add(espacosClasse(distr.getString("id").toInt(),

                distr.getString("Nome").toString(), distr.getString("EstadoId").toInt(),
                distr.getString("Motivo_Bloqueio").toString()

            ))
        }
    } catch (e: JSONException) {
        e.printStackTrace()
    }
    }, { error -> error.printStackTrace() })
    requestQueue?.add(request)
}