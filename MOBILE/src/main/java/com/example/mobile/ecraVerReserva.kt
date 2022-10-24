package com.example.mobile

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class ecraVerReserva : AppCompatActivity() {
    var f_horafim=""
    var f_idsala=""
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecra_ver_reserva)

        var dataReserva = findViewById<EditText>(R.id.data)
        var horaInicio = findViewById<EditText>(R.id.horaInicio)
        var horaFim = findViewById<EditText>(R.id.horaFim)
        var btnReservar=findViewById<Button>(R.id.btnReservar)
        var btnVoltar=findViewById<Button>(R.id.voltar)
        var btn_adiar=findViewById<Button>(R.id.btn_adiar)
        var btn_Delete=findViewById<Button>(R.id.btn_Delete)
        var btn_terminar=findViewById<Button>(R.id.btn_terminar)
        var img_qrcode=findViewById<ImageView>(R.id.img_qrcode)

        btnReservar.setInputType(InputType.TYPE_NULL)
        horaInicio.setInputType(InputType.TYPE_NULL)
        horaFim.setInputType(InputType.TYPE_NULL)

        val Estado=intent.getStringExtra("Estado").toString()
        var DataReserva=intent.getStringExtra("DataReserva").toString()
        var IDSala=intent.getStringExtra("IDSala").toString()
        var HoraInicio=intent.getStringExtra("HoraInicio").toString()
        var HoraFim=intent.getStringExtra("HoraFim").toString()
        val idReserva=intent.getStringExtra("idReserva").toString()
        var estado=intent.getStringExtra("Estado").toString()
        var activity=intent.getStringExtra("passa").toString()


        carregarReserva(idReserva)
        verifica(Estado,DataReserva,HoraInicio,HoraFim)
        img_qrcode(IDSala)

        //Botao voltar a tras
        btnVoltar.setOnClickListener {
            if(activity=="1") {
                val inte = Intent(this, ecraListaReservasAtuais::class.java)
                startActivity(inte)
            }
            else if(activity=="2") {
                val inte = Intent(this, ecraListaReservasPassadas::class.java)
                startActivity(inte)
            }
        }

        //Editar Reserva
        btnReservar.setOnClickListener {
            Update(idReserva)
        }

        //Adiar
        btn_adiar.setOnTouchListener {view, motionEvent ->
            if (motionEvent.getAction() === MotionEvent.ACTION_UP)
                popAdiarUphora(horaFim,idReserva)
            false
        }

        //Terminar
        btn_terminar.setOnTouchListener {view, motionEvent ->
            var n=0
            if (motionEvent.getAction() === MotionEvent.ACTION_UP)
                PopUpConfirmar(n,idReserva,IDSala)
            false
        }

        //Adiar
        btn_Delete.setOnTouchListener {view, motionEvent ->
            var n=1
            if (motionEvent.getAction() === MotionEvent.ACTION_UP)
                PopUpConfirmar(n,idReserva,IDSala)
            false
        }

        // escolher a data
        dataReserva.setOnTouchListener { view, motionEvent ->
            if (motionEvent.getAction() === MotionEvent.ACTION_UP)
                popupData(idReserva)
            false
        }

        // ecolher a hora inicio
        horaInicio.setOnTouchListener { view, motionEvent ->
            if (motionEvent.getAction() === MotionEvent.ACTION_UP)
                popUphora(horaInicio,idReserva)
            false
        }
        // ecolher a hora inicio
        horaFim.setOnTouchListener { view, motionEvent ->
            if (motionEvent.getAction() === MotionEvent.ACTION_UP)
                popUphoraFim(horaFim,idReserva)
            false
        }

        //PopUp//TODO CLICK POP QRCODE
        img_qrcode.setOnClickListener{
            get_qrcodePOPUP(IDSala)
        }
    }
    //TODO COMPOR CODIGO
    //Carrega a reserva selecionada
    fun carregarReserva(idReserva :String){
        var etiqueta = findViewById<EditText>(R.id.etiqueta)
        var dataReserva = findViewById<EditText>(R.id.data)
        var horaInicio = findViewById<EditText>(R.id.horaInicio)
        var horaFim = findViewById<EditText>(R.id.horaFim)

        var url = "https://softinsa.herokuapp.com/reservas/get/".plus(idReserva)
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try{
                val data = response.getJSONArray("" + "data")
                for (i in 0 until data.length()) {
                    val distr = data.getJSONObject(0)
                    val vetiqueta = distr.getString("NomeReserva")
                    val idSala = distr.getString("SalaId")
                    val nparticipantes= distr.getString("NumeroParticipantes")
                    val datareserva = distr.getString("DataReserva")
                    val horainicio = distr.getString("HoraInicio")
                    val horafim = distr.getString("HoraFim")

                    etiqueta.setText(vetiqueta)
                    dataReserva.setText(datareserva)
                    horaInicio.setText(horainicio)
                    horaFim.setText(horafim)
                    f_horafim =horafim.toString()
                    f_idsala=idSala.toString()
                    carregarSala(idSala,nparticipantes)

                }
            }catch (e:JSONException){
                e.printStackTrace()
            }
        }, {error -> error.printStackTrace()})
        queue.add(request)
    }
    //TODO COMPOR CODIGO
    //Verifica tipo de reserva
    fun verifica(Estado:String,DataReserva:String,HoraInicio:String,HoraFim:String){

        var btnReservar=findViewById<Button>(R.id.btnReservar)
        var btn_adiar=findViewById<Button>(R.id.btn_adiar)
        var btn_terminar=findViewById<Button>(R.id.btn_terminar)
        var btn_Delete=findViewById<Button>(R.id.btn_Delete)

        if(Estado=="2"){
            enable()
            btn_adiar.visibility=Button.GONE
            btnReservar.visibility=Button.GONE
            btn_terminar.visibility=Button.GONE
            btn_Delete.visibility = Button.GONE
        }
        if(Estado=="1"){
            desable()
            val date = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val strDate = formatter.format(date)
            val data_hoje=LocalDate.parse(strDate,DateTimeFormatter.ISO_DATE)
            val data=LocalDate.parse(DataReserva, DateTimeFormatter.ISO_DATE)

            val time_i: String = HoraInicio.toString()
            val time_f: String = HoraFim.toString()
            val time = LocalTime.now()
            val horaI = LocalTime.parse(time_i)
            val horaF = LocalTime.parse(time_f)
            val valori = time.compareTo(horaI)
            val valorf = horaF.compareTo(time)

            if(data==data_hoje){
                if(valori==0||valori==1){
                    if (valorf == 0 || valorf == 1) {
                        //Adiar//Terminar
                        enable()
                        btn_adiar.visibility = Button.VISIBLE
                        btn_terminar.visibility=Button.VISIBLE
                        btnReservar.visibility = Button.GONE
                        btn_Delete.visibility = Button.GONE
                    } else {
                        enable()
                        btn_adiar.visibility = Button.GONE
                        btn_terminar.visibility=Button.GONE
                        btnReservar.visibility = Button.GONE
                        btn_Delete.visibility = Button.GONE
                    }
                } else {
                    //Delete
                        desable()
                    btn_adiar.visibility = Button.GONE
                    btn_terminar.visibility=Button.GONE
                    btn_Delete.visibility=Button.VISIBLE
                    btnReservar.visibility = Button.VISIBLE
                }
            } else if(data>data_hoje){
                //Delete
                btn_adiar.visibility = Button.GONE
                btn_terminar.visibility=Button.GONE
                btn_Delete.visibility=Button.VISIBLE
                btnReservar.visibility = Button.VISIBLE
            }
            if(data<data_hoje) {
                enable()
                btn_adiar.visibility=Button.GONE
                btn_terminar.visibility=Button.GONE
                btn_Delete.visibility=Button.GONE
                btnReservar.visibility=Button.GONE
            }

        }
    }
    //TODO COMPOR CODIGO
    //carrega sala para o nomeEspaço
    fun carregarSala(idSala:String,np:String){
        var nomeEspaco = findViewById<TextView>(R.id.nomeEspaco)
        var participantes = findViewById<NumberPicker>(R.id.numeroParticipantes)
        var numeroParticipantes = findViewById<TextView>(R.id.maxParticipantes)

        var url = "https://softinsa.herokuapp.com/salas/get/".plus(idSala)
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try{
                val distr = response.getJSONObject("data")
                nomeEspaco.text = distr.getString("Nome").toString()
                var alocacao = distr.getInt("Alocacao")
                var capacidade = distr.getInt("Capacidade")
                numeroParticipantes.setText("(max " + alocacao * capacidade / 100 + ")")
                participantes.maxValue = alocacao * capacidade / 100
                participantes.minValue=1
                participantes.value=np.toInt()
            }catch (e:JSONException){
                e.printStackTrace()
            }

        }, {error -> error.printStackTrace()})
        queue.add(request)
    }
    //TODO COMPOR CODIGO
    fun popupData(idReserva:String)
    {
        val bottomSheetDialog= BottomSheetDialog(this, R.style.BottomSheetTheme)
        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.layout_bottom_sheet,
            findViewById<LinearLayout>(R.id.bottomSheet)
        )

        val current = LocalDateTime.now()
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val strDate = formatter.format(date)
        val data_hoje=LocalDate.parse(strDate,DateTimeFormatter.ISO_DATE)

        var mes = bottomSheetView.findViewById<NumberPicker>(R.id.mes)
        var dia = bottomSheetView.findViewById<NumberPicker>(R.id.dia)
        var ano = bottomSheetView.findViewById<NumberPicker>(R.id.ano)

        ano.minValue=current.year
        ano.maxValue = (current.year)+3

        var url = "https://softinsa.herokuapp.com/reservas/get/".plus(idReserva)
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try{
                val data = response.getJSONArray("" + "data")
                for (i in 0 until data.length()) {
                    val distr = data.getJSONObject(0)
                    val datareserva = distr.getString("DataReserva")
                    var vano=datareserva.substringBefore('-')
                    var vmes=datareserva.substringAfter(vano+'-').substringBefore('-')
                    var vdia=datareserva.substringAfter(vmes+'-')

                    ano.value=vano.toInt()
                    mes.value=vmes.toInt()
                    dia.value=vdia.toInt()
                }
            }catch (e:JSONException){
                e.printStackTrace()
            }

        }, {error -> error.printStackTrace()})
        queue.add(request)

        dia.minValue = 1
        dia.maxValue = 31

        mes.minValue = 1
        mes.maxValue = 12


        var btnEscolher = bottomSheetView.findViewById<Button>(R.id.btnEscolherData)
        ano.setOnValueChangedListener(NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->

            if(ano.value==data_hoje.year){
                mes.minValue = data_hoje.monthValue
                if(mes.value==data_hoje.monthValue){
                    dia.minValue=data_hoje.dayOfMonth
                }
                if(mes.value>data_hoje.monthValue){
                    dia.minValue=1
                }
            }
            if(ano.value>data_hoje.year){
                mes.minValue = 1
                dia.minValue=1
            }

        })

        mes.setOnValueChangedListener(NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->

            if(ano.value==data_hoje.year){
                mes.minValue = data_hoje.monthValue
                    if(mes.value==data_hoje.monthValue) {
                            if(mes.value==data_hoje.monthValue) {
                                dia.minValue = data_hoje.dayOfMonth
                                if (mes.value == 6 || mes.value == 4 || mes.value == 9 || mes.value == 11)
                                    dia.maxValue = 30
                                else if (mes.value == 2)
                                    dia.maxValue = 28
                                else
                                    dia.maxValue = 31
                            }
                        if (mes.value == 6 || mes.value == 4 || mes.value == 9 || mes.value == 11)
                            dia.maxValue = 30
                        else if (mes.value == 2)
                            dia.maxValue = 28
                        else
                            dia.maxValue = 31
                    }
                    if(mes.value!=data_hoje.monthValue){
                        dia.minValue = 1
                        if (mes.value == 6 || mes.value == 4 || mes.value == 9 || mes.value == 11)
                            dia.maxValue = 30
                        else if (mes.value == 2)
                            dia.maxValue = 28
                        else
                            dia.maxValue = 31
                    }
            }
            if(ano.value>data_hoje.year){
                if (mes.value == 6 || mes.value == 4 || mes.value == 9 || mes.value == 11)
                    dia.maxValue = 30
                else if (mes.value == 2)
                    dia.maxValue = 28
                else
                    dia.maxValue = 31
            }
        })
        dia.setOnValueChangedListener(NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->

            if(ano.value==data_hoje.year){
                mes.minValue = data_hoje.monthValue
                if(mes.value==data_hoje.monthValue) {
                    if(mes.value==data_hoje.monthValue) {
                        dia.minValue = data_hoje.dayOfMonth
                        if (mes.value == 6 || mes.value == 4 || mes.value == 9 || mes.value == 11)
                            dia.maxValue = 30
                        else if (mes.value == 2)
                            dia.maxValue = 28
                        else
                            dia.maxValue = 31
                    }
                    if (mes.value == 6 || mes.value == 4 || mes.value == 9 || mes.value == 11)
                        dia.maxValue = 30
                    else if (mes.value == 2)
                        dia.maxValue = 28
                    else
                        dia.maxValue = 31
                }
                if(mes.value!=data_hoje.monthValue){
                    dia.minValue = 1
                    if (mes.value == 6 || mes.value == 4 || mes.value == 9 || mes.value == 11)
                        dia.maxValue = 30
                    else if (mes.value == 2)
                        dia.maxValue = 28
                    else
                        dia.maxValue = 31
                }
            }
            if(ano.value>data_hoje.year){
                if (mes.value == 6 || mes.value == 4 || mes.value == 9 || mes.value == 11)
                    dia.maxValue = 30
                else if (mes.value == 2)
                    dia.maxValue = 28
                else
                    dia.maxValue = 31
            }
        })

        btnEscolher.setOnClickListener {
            var diaFormatada = String.format("%02d", dia.getValue())
            var mesFormatado = String.format("%02d", mes.getValue())
            findViewById<EditText>(R.id.data).setText(ano.value.toString().plus("-").plus(mesFormatado).plus("-").plus(diaFormatada)).toString()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }
    //TODO COMPOR CODIGO
    fun popUphora(input : EditText,idReserva:String)
    {
        var asset = assets(this)
        val bottomSheetDialog= BottomSheetDialog(this, R.style.BottomSheetTheme)
        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.layout_bottom_sheet_hora,
            findViewById<LinearLayout>(R.id.bottomSheet)
        )

        var hora = bottomSheetView.findViewById<NumberPicker>(R.id.hora)
        hora.minValue = 0
        hora.maxValue = 59

        var minutos = bottomSheetView.findViewById<NumberPicker>(R.id.minuto)
        minutos.minValue = 0
        minutos.maxValue = 59


        hora.setFormatter(NumberPicker.Formatter { i -> String.format("%02d", i) })
        minutos.setFormatter(NumberPicker.Formatter { i -> String.format("%02d", i) })
        var url = "https://softinsa.herokuapp.com/reservas/get/".plus(idReserva)
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try{
                val data = response.getJSONArray("" + "data")
                for (i in 0 until data.length()) {
                    val distr = data.getJSONObject(0)
                    val horaInicio = distr.getString("HoraInicio")
                    var vhora=horaInicio.substringBefore(':')
                    var vminuto=horaInicio.substringAfter(vhora+':').substringBefore(':')
                    hora.value=vhora.toInt()
                    minutos.value=vminuto.toInt()

                }
            }catch (e:JSONException){
                e.printStackTrace()
            }

        }, {error -> error.printStackTrace()})
        queue.add(request)

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
    //TODO COMPOR CODIGO
    fun popUphoraFim(input : EditText,idReserva:String)
    {
        var dataReserva = findViewById<EditText>(R.id.data)
        val bottomSheetDialog= BottomSheetDialog(this, R.style.BottomSheetTheme)
        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.layout_bottom_sheet_hora,
            findViewById<LinearLayout>(R.id.bottomSheet)
        )

        var currentDateTime=LocalDateTime.now()
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val strDate = formatter.format(date)
        val data_hoje=LocalDate.parse(strDate,DateTimeFormatter.ISO_DATE)


        var hora = bottomSheetView.findViewById<NumberPicker>(R.id.hora)
        hora.minValue = 0
        hora.maxValue = 23


        var minutos = bottomSheetView.findViewById<NumberPicker>(R.id.minuto)
        minutos.minValue = 0
        minutos.maxValue = 59

        var datatext=LocalDate.parse(dataReserva.text,DateTimeFormatter.ISO_DATE)
        if(datatext==data_hoje){
            hora.minValue=currentDateTime.hour
        }

        hora.setFormatter(NumberPicker.Formatter { i -> String.format("%02d", i) })
        minutos.setFormatter(NumberPicker.Formatter { i -> String.format("%02d", i) })
        var url = "https://softinsa.herokuapp.com/reservas/get/".plus(idReserva)
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try{
                val data = response.getJSONArray("" + "data")
                for (i in 0 until data.length()) {
                    val distr = data.getJSONObject(0)
                    val horaInicio = distr.getString("HoraFim")
                    var vhora=horaInicio.substringBefore(':')
                    var vminuto=horaInicio.substringAfter(vhora+':').substringBefore(':')

                    hora.value=vhora.toInt()
                    minutos.value=vminuto.toInt()
                }
            }catch (e:JSONException){
                e.printStackTrace()
            }

        }, {error -> error.printStackTrace()})
        queue.add(request)

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
    //TODO COMPOR CODIGO
    fun popAdiarUphora(input : EditText,idReserva:String)
    {
        val bottomSheetDialog= BottomSheetDialog(this, R.style.BottomSheetTheme)
        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.layout_bottom_sheet_hora,
            findViewById<LinearLayout>(R.id.bottomSheet)
        )

        var hora = bottomSheetView.findViewById<NumberPicker>(R.id.hora)
        hora.minValue = 0
        hora.maxValue = 23


        var minutos = bottomSheetView.findViewById<NumberPicker>(R.id.minuto)
        minutos.minValue = 0
        minutos.maxValue = 59

        hora.setFormatter(NumberPicker.Formatter { i -> String.format("%02d", i) })
        minutos.setFormatter(NumberPicker.Formatter { i -> String.format("%02d", i) })
        var url = "https://softinsa.herokuapp.com/reservas/get/".plus(idReserva)
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try{
                val data = response.getJSONArray("" + "data")
                for (i in 0 until data.length()) {
                    val distr = data.getJSONObject(0)
                    val HoraFim = distr.getString("HoraFim")
                    var vhora=HoraFim.substringBefore(':')
                    var vminuto=HoraFim.substringAfter(vhora+':').substringBefore(':')

                    hora.value=vhora.toInt()
                    minutos.value=vminuto.toInt()
                    hora.minValue=vhora.toInt()
                    minutos.minValue=vminuto.toInt()

                    hora.setOnValueChangedListener(NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
                        if(hora.value==vhora.toInt()){
                            minutos.minValue=vminuto.toInt()
                        }
                        if(hora.value>vhora.toInt()){
                            minutos.minValue=0
                        }

                    })
                    minutos.setOnValueChangedListener(NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
                        if(hora.value==vhora.toInt()){
                            minutos.minValue=vminuto.toInt()
                        }
                        if(hora.value>vhora.toInt()){
                            minutos.minValue=0
                        }

                    })
                }
            }catch (e:JSONException){
                e.printStackTrace()
            }

        }, {error -> error.printStackTrace()})
        queue.add(request)

        var btnEscolher = bottomSheetView.findViewById<Button>(R.id.btnEscolherData)
        btnEscolher.setOnClickListener {
            var horaFormatada = String.format("%02d", hora.getValue())
            var minutosFormatado = String.format("%02d", minutos.getValue())
            input.setText(horaFormatada.plus(":").plus(minutosFormatado))
            //proReserva()
            adiar(idReserva)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    //TODO COMPOR CODIGO
    //Adiar Reserva
    fun adiar(idReserva:String){
        var horaFim = findViewById<EditText>(R.id.horaFim)
        horaFim.setInputType(InputType.TYPE_NULL)

        var requestQueue = Volley.newRequestQueue(this)
        var url = "https://softinsa.herokuapp.com/reservas/adiar/$idReserva"

        val StringRequest =
            object : StringRequest(Request.Method.PUT, url, Response.Listener { response ->

                val data = JSONObject(response)
                val message: String = data.optString("message")
                val sucesso: String = data.optString("sucesso")
                if (sucesso.contains("true")) {
                    startActivity(Intent(this, ecraListaReservasAtuais::class.java))
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener { error ->
            }){
                override fun getParams(): MutableMap<String, String>? {

                    val map=HashMap<String,String>()
                    map["ValorHora"]= horaFim.text.toString()

                    return map
                }

            }
        requestQueue?.add(StringRequest)
    }
    //TODO COMPOR CODIGO
    //Editar Reserva
    fun Update(idReserva :String){
        var etiqueta = findViewById<EditText>(R.id.etiqueta)
        var participantes = findViewById<NumberPicker>(R.id.numeroParticipantes)
        var dataReserva = findViewById<EditText>(R.id.data)
        var horaInicio = findViewById<EditText>(R.id.horaInicio)
        var horaFim = findViewById<EditText>(R.id.horaFim)

        var asset = assets(this)
        var url = "https://softinsa.herokuapp.com/reservas/update/$idReserva"
        val queue = Volley.newRequestQueue(this)
        val request = object : StringRequest( Method.PUT, url, Response.Listener { response ->

            val data = JSONObject(response)
            val message: String = data.optString("message")
            val sucesso: String = data.optString("sucesso")

            if (sucesso.contains("true")) {
                startActivity(Intent(this, ecraListaReservasAtuais::class.java))
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            if (sucesso.contains("false")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener {})
        {
            override fun getParams(): Map<String, String>? {

                val params: MutableMap<String, String> = HashMap()

                if (etiqueta.text.toString() == "")
                    params["NomeReserva"] = " "
                else
                    params["NomeReserva"] = etiqueta.text.toString()

                params["DataReserva"] = dataReserva.text.toString()
                params["NumeroParticipantes"] = (participantes.value).toString()
                params["HoraInicio"] = horaInicio.text.toString()
                params["HoraFim"] = horaFim.text.toString()
                params["Utilizador"] = asset.obterIDUser().toString()
                params["Sala"] = f_idsala

                return params
            }
        }
        queue.add(request)
    }
    fun enable(){
        var etiqueta = findViewById<EditText>(R.id.etiqueta)
        var participantes = findViewById<NumberPicker>(R.id.numeroParticipantes)
        var dataReserva = findViewById<EditText>(R.id.data)
        var horaInicio = findViewById<EditText>(R.id.horaInicio)
        var horaFim = findViewById<EditText>(R.id.horaFim)

        participantes.isEnabled=false
        etiqueta.isEnabled=false
        dataReserva.isEnabled=false
        horaInicio.isEnabled=false
        horaFim.isEnabled=false
    }
    fun desable(){
        var etiqueta = findViewById<EditText>(R.id.etiqueta)
        var participantes = findViewById<NumberPicker>(R.id.numeroParticipantes)
        var dataReserva = findViewById<EditText>(R.id.data)
        var horaInicio = findViewById<EditText>(R.id.horaInicio)
        var horaFim = findViewById<EditText>(R.id.horaFim)

        participantes.isEnabled=true
        etiqueta.isEnabled=true
        dataReserva.isEnabled=true
        horaInicio.isEnabled=true
        horaFim.isEnabled=true
    }
    //TODO COMPOR CODIGO
    //Terminar mais cedo
    fun End(reservasID:String,SalaId: String){
        val queue_terminar = Volley.newRequestQueue(this)
        val url = "https://softinsa.herokuapp.com/reservas/terminar/$reservasID"

        val sr_terminar: StringRequest = object : StringRequest(Method.PUT, url, Response.Listener {response->
            val data = JSONObject(response)
            val message: String = data.optString("message")
            val sucesso: String = data.optString("sucesso")
            if (sucesso.contains("true")) {
                startActivity(Intent(this, ecraListaReservasAtuais::class.java))
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        },
            Response.ErrorListener { error -> Log.e("HttpClient", "error: $error") })
        {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = java.util.HashMap()
                params["SalaId"] = SalaId.toString()
                return params
            }
        }
        queue_terminar.add(sr_terminar)
    }
    //TODO COMPOR CODIGO
    //Desativar Reunião
    fun delete(reservasID:String){
        val queue = Volley.newRequestQueue(this)
        val url = "https://softinsa.herokuapp.com/reservas/desativar/".plus(reservasID)
        val sr: StringRequest = object : StringRequest(Method.PUT, url, Response.Listener { response->

            val data = JSONObject(response)
            val message: String = data.optString("message")
            val sucesso: String = data.optString("sucesso")
            if (sucesso.contains("true")) {
                startActivity(Intent(this, ecraListaReservasAtuais::class.java))
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        },
            Response.ErrorListener { error -> Log.e("HttpClient", "error: $error") }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["IdReserva"] = reservasID
                return params
            }
        }
        queue.add(sr)
    }
    //POP UP Confirmar para evitar acidentes
    fun PopUpConfirmar(n:Int,reservasID:String,SalaId:String)
    {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.layout_bottom_sheet_confirmar,
            findViewById<LinearLayout>(R.id.bottomSheet)
        )

        var btn_Confirmar = bottomSheetView.findViewById<Button>(R.id.btn_Confirmar)
        var txt_confirmar = bottomSheetView.findViewById<TextView>(R.id.txt_confirmar)

        if (n==0) {
            txt_confirmar.text="Dejesa terminar mais cedo?"
            btn_Confirmar.setOnClickListener {
                End(reservasID,SalaId)
                bottomSheetDialog.dismiss()
            }
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }
        else if (n==1) {
            txt_confirmar.text="Deseja cancelar a reunião?"

            btn_Confirmar.setOnClickListener {
                delete(reservasID)
                bottomSheetDialog.dismiss()
            }
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }
    }
    fun img_qrcode(IDSala:String){

        var SalaQueue = Volley.newRequestQueue(this)
        var urlsala = "https://softinsa.herokuapp.com/salas/codigo/".plus(IDSala)

        val request_sala =
            object : StringRequest(Method.GET, urlsala, Response.Listener { response ->
                try {

                    val Codigo_Sala = JSONObject(response)
                    val Data_QRcode=Codigo_Sala.optString("data")
                    val QRcode=Data_QRcode.substringAfter("data:image/png;base64,")
                    val imageBytes=Base64.decode(QRcode,Base64.DEFAULT)
                    val decodedImage=BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
                    var img_qrcode=findViewById<ImageView>(R.id.img_qrcode)
                    img_qrcode.setImageBitmap(decodedImage)


                } catch (e: JSONException) {
                    e.printStackTrace()
                } }, Response.ErrorListener { error -> error.printStackTrace() })
            {
            }
        SalaQueue?.add(request_sala)
    }
    fun get_qrcodePOPUP(SalaId: String){
        val bottomSheetDialog= BottomSheetDialog(this, R.style.BottomSheetTheme)
        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.layout_bottom_sheet_qrcode,
            findViewById<LinearLayout>(R.id.bottomSheet)
        )

        var txt_nomesala = bottomSheetView.findViewById<TextView>(R.id.txt_nomesala)
        var ImagemQR = bottomSheetView.findViewById<ImageView>(R.id.ImagemQR)

        var SalaQueue = Volley.newRequestQueue(this)
        var urlsala = "https://softinsa.herokuapp.com/salas/codigo/".plus(SalaId)
        val request_sala =
            object : StringRequest(Method.GET, urlsala, Response.Listener { response ->
                try {

                    val Codigo_Sala = JSONObject(response)
                    val Data_QRcode=Codigo_Sala.optString("data")
                    val QRcode=Data_QRcode.substringAfter("data:image/png;base64,")
                    val imageBytes=Base64.decode(QRcode,Base64.DEFAULT)
                    val decodedImage=BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)

                    ImagemQR.setImageBitmap(decodedImage)

                    var NomeSalaQueue = Volley.newRequestQueue(this)
                    var urlNomesala = "https://softinsa.herokuapp.com/salas/get/".plus(SalaId)
                    val request_nomSala =
                        JsonObjectRequest(Request.Method.GET, urlNomesala, null, Response.Listener { response ->
                            try {
                                val sala = response.getJSONObject("data")
                                val Nome_Sala = sala.getString("Nome")

                                txt_nomesala.setText(Nome_Sala)

                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }, Response.ErrorListener { error -> error.printStackTrace() })
                    NomeSalaQueue?.add(request_nomSala)


                } catch (e: JSONException) {
                    e.printStackTrace()
                } }, Response.ErrorListener { error -> error.printStackTrace() })
            {
            }
        SalaQueue?.add(request_sala)

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }
}