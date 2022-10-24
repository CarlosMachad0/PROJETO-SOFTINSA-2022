package com.example.mobile

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.*
import android.widget.NumberPicker.OnValueChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.widget.addTextChangedListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class ecraReserva : AppCompatActivity() {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        var idEspaco = intent.getStringExtra("idEspaco").toString()

        verificarUtilizador(idEspaco) // verifica se o utilizador pertence ao centro
    }

    var horaInicio = 0
    var horaFim = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecra_reserva)

        var idEspaco = intent.getStringExtra("idEspaco").toString()

        verificarUtilizador(idEspaco) // verifica se o utilizador pertence ao centro

        var etiqueta = findViewById<EditText>(R.id.etiqueta)
        var nomeEspaco = findViewById<TextView>(R.id.nomeEspaco)
        var participantes = findViewById<NumberPicker>(R.id.numeroParticipantes)
        var numeroParticipantes = findViewById<TextView>(R.id.maxParticipantes)
        var data = findViewById<EditText>(R.id.data)
        var horaInicio = findViewById<EditText>(R.id.horaInicio)
        var horaFim = findViewById<EditText>(R.id.horaFim)
        var btnVoltar = findViewById<Button>(R.id.voltar)
        var btnReservar = findViewById<Button>(R.id.btnReservar)

        data.setInputType(InputType.TYPE_NULL)
        horaInicio.setInputType(InputType.TYPE_NULL)
        horaFim.setInputType(InputType.TYPE_NULL)

        /*
        etiqueta.addTextChangedListener {
            if (etiqueta.text.toString() != "")
                findViewById<TextView>(R.id.labelEtiqueta).visibility = TextView.VISIBLE
            else
                findViewById<TextView>(R.id.labelEtiqueta).visibility = TextView.INVISIBLE
        }

        data.addTextChangedListener {
            if (data.text.toString() != "")
                findViewById<TextView>(R.id.labelData).visibility = TextView.VISIBLE
            else
                findViewById<TextView>(R.id.labelData).visibility = TextView.INVISIBLE
        }

        horaInicio.addTextChangedListener {
            if (horaInicio.text.toString() != "")
                findViewById<TextView>(R.id.labelHoraInicio).visibility = TextView.VISIBLE
            else
                findViewById<TextView>(R.id.labelHoraInicio).visibility = TextView.INVISIBLE
        }

        horaFim.addTextChangedListener {
            if (horaFim.text.toString() != "")
                findViewById<TextView>(R.id.labelHoraFim).visibility = TextView.VISIBLE
            else
                findViewById<TextView>(R.id.labelHoraFim).visibility = TextView.INVISIBLE
        }

         */

        // carregar detalhes da sala

        var url = "https://softinsa.herokuapp.com/salas/get/".plus(idEspaco)
        val requestQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val distr = response.getJSONObject("data")
                nomeEspaco.text = distr.getString("Nome").toString()
                var alocacao = distr.getInt("Alocacao")
                var capacidade = distr.getInt("Capacidade")
                numeroParticipantes.setText("(max " + alocacao * capacidade / 100 + ")")
                participantes.maxValue = alocacao * capacidade / 100

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, { error -> error.printStackTrace() })
        requestQueue?.add(request)

        // numeroParticipantes

        participantes.minValue = 1
        participantes.value = 1


        // escolher a data

        data.setOnTouchListener { view, motionEvent ->
            if (motionEvent.getAction() === MotionEvent.ACTION_UP)
               popupData()
            false
        }

        // ecolher a hora inicio

        horaInicio.setOnTouchListener { view, motionEvent ->
            if (motionEvent.getAction() === MotionEvent.ACTION_UP)
              popUphora(horaInicio)
            false
        }

        // esolher hora fim

        horaFim.setOnTouchListener { view, motionEvent ->
            if (motionEvent.getAction() === MotionEvent.ACTION_UP)
               popUphora(horaFim)
            false
        }

        // botao voltar

        btnVoltar.setOnClickListener {
            finish()
        }

        // botao reservar

        btnReservar.setOnClickListener {
            if (data.text.toString() != "" && horaInicio.text.toString() != "" && horaFim.text.toString() != "")
                reservar(data.text.toString(), horaInicio.text.toString(), horaFim.text.toString(), etiqueta.text.toString(), idEspaco, participantes.value.toString())
        }

    }

    fun popUphora(input : EditText)
    {
        val bottomSheetDialog= BottomSheetDialog(this, R.style.BottomSheetTheme)
        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.layout_bottom_sheet_hora,
            findViewById<LinearLayout>(R.id.bottomSheet)
        )

        var hora = bottomSheetView.findViewById<NumberPicker>(R.id.hora)
        hora.minValue = 0
        hora.value = 8
        hora.maxValue = 23


        var minutos = bottomSheetView.findViewById<NumberPicker>(R.id.minuto)
        minutos.minValue = 0
        minutos.value = 0
        minutos.maxValue = 59

        hora.setFormatter(NumberPicker.Formatter { i -> String.format("%02d", i) })
        minutos.setFormatter(NumberPicker.Formatter { i -> String.format("%02d", i) })

        var btnEscolher = bottomSheetView.findViewById<Button>(R.id.btnEscolherData)

        btnEscolher.setOnClickListener {
            var horaFormatada = String.format("%02d", hora.getValue())
            var minutosFormatado = String.format("%02d", minutos.getValue())
            input.setText(horaFormatada.plus(":").plus(minutosFormatado))
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    fun popupData()
    {
        val bottomSheetDialog= BottomSheetDialog(this, R.style.BottomSheetTheme)
        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.layout_bottom_sheet,
            findViewById<LinearLayout>(R.id.bottomSheet)
        )

        val calendar = Calendar.getInstance()

        var dia = bottomSheetView.findViewById<NumberPicker>(R.id.dia)
        dia.minValue = 1
        dia.value = 1
        dia.maxValue = 31


        var mes = bottomSheetView.findViewById<NumberPicker>(R.id.mes)
        mes.minValue = 1
        mes.value = 1
        mes.maxValue = 12

        var ano = bottomSheetView.findViewById<NumberPicker>(R.id.ano)
        ano.minValue = 2022
        ano.value = 2022
        ano.maxValue = 2025

        var btnEscolher = bottomSheetView.findViewById<Button>(R.id.btnEscolherData)

        mes.setOnValueChangedListener(OnValueChangeListener { picker, oldVal, newVal ->
            if (mes.value == 6 || mes.value == 4 || mes.value == 9 || mes.value == 11)
                dia.maxValue = 30
            else if (mes.value == 2)
                dia.maxValue = 28
            else
                dia.maxValue = 31
        })

        btnEscolher.setOnClickListener {
            findViewById<EditText>(R.id.data).setText(ano.value.toString().plus("-").plus(mes.value.toString()).plus("-").plus(dia.value.toString())).toString()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    fun reservar(data : String, horaInicio : String, horaFim : String, etiqueta : String, idEspaco : String, participantes : String)
    {
        var url = "https://softinsa.herokuapp.com/reservas/add"
        val queue = Volley.newRequestQueue(this)
        var asset = assets(this)

        val request = object : StringRequest( Method.POST, url, Response.Listener { response ->

            val data = JSONObject(response)
            val message: String = data.optString("message")
            val sucesso: String = data.optString("sucesso")

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            if (sucesso == "true")
                startActivity(Intent(this,ecraListaReservasAtuais::class.java))

        }, Response.ErrorListener {})
        {
            override fun getParams(): Map<String, String>? {

                val params: MutableMap<String, String> = HashMap()


                if (etiqueta == "")
                    params["NomeReserva"] = " "
                else
                    params["NomeReserva"] = etiqueta

                params["DataReserva"] = data
                params["NumeroParticipantes"] = participantes.toString()
                params["HoraInicio"] = horaInicio
                params["HoraFim"] = horaFim
                params["Utilizador"] = asset.obterIDUser().toString()
                params["Sala"] = idEspaco

                return params
            }
        }
        queue.add(request)
    }

    fun verificarUtilizador(idEspaco : String)
    {
        var asset = assets(this)
        var url = "https://softinsa.herokuapp.com/salas/utilizador/".plus(asset.obterIDUser()).plus("/").plus(idEspaco)

        val requestQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->try {

            //Toast.makeText(this, response.optString("dados"), Toast.LENGTH_SHORT).show()

            if (!response.optString("dados").toBoolean())
                startActivity(Intent(this, MainActivity::class.java))

        } catch (e: JSONException) { e.printStackTrace() }
        }, { error -> error.printStackTrace() })
        requestQueue.add(request)
    }
}