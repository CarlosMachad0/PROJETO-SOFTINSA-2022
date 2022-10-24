package com.example.mobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class ecraListaReservasAtuais : AppCompatActivity() {
    lateinit var actionBarDrawerToggle : ActionBarDrawerToggle
    var listaReservasAtuais = ArrayList<Reservas>()
    override fun onResume() {
        super.onResume()

        var asset = assets(applicationContext)
        if (asset.obterIDUser() == 0)
            startActivity(Intent(this, ecraLogin::class.java))

        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.menu.getItem(3).isChecked = true

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecra_lista_reservas_atuais)

        var asset = assets(applicationContext)
        if (asset.obterIDUser() == 0)
            startActivity(Intent(this, ecraLogin::class.java))

        //TODO DELETE DE RESERVAS PASSADAS
        //carregadadosreserva()

        var listaCentros = ArrayList<Centros>()
        var listaFiltradaAtuais = ArrayList<Reservas>()
        var procurarReservasAtuais = findViewById<SearchView>(R.id.searchView)

        //listaReservas()

        //Menu botoes
        var btn_historico = findViewById<Button>(R.id.btn_historico)
        btn_historico.setOnClickListener {
            startActivity(Intent(this, ecraListaReservasPassadas::class.java))
        }
        var btn_atuais = findViewById<Button>(R.id.btn_atuais)
        btn_atuais.setOnClickListener { refresh() }

        // carregar menu lateral
        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        carregarMenu(listaCentros)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.perfil -> {
                    startActivity(Intent(this, ecraPerfil::class.java))
                }
                R.id.qrcode -> {
                    startActivity(Intent(this, ecraQRCode::class.java))
                }
                R.id.espacos -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                R.id.reservas -> {
                    refresh()
                }
                R.id.sair -> {
                    asset.apagarDados()
                    startActivity(Intent(this, ecraLogin::class.java))
                }
            }
            true
        }

        // quando um item do spinner e clicado

        var spinner = navigationView.getHeaderView(0).findViewById<Spinner>(R.id.spinnerCentros)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                listaReservasAtuais.clear()
                asset.inserirIDCentro(listaCentros[position].IDCentro)
                carregarDados(asset.obterIDUser().toString(),asset.obterIDCentro().toString(),listaReservasAtuais)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        // quando um item e procurado na lista

        procurarReservasAtuais.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {

                listaFiltradaAtuais.clear()

                for (Reserva in listaReservasAtuais)
                {
                    if (Reserva.NomeReserva.contains(query, ignoreCase = true)||Reserva.NomeSala.contains(query, ignoreCase = true))
                        listaFiltradaAtuais.add(Reserva)
                }

                var adaptadorLista = adaptadorListaReservas(applicationContext, listaFiltradaAtuais)
                findViewById<ListView>(R.id.lista).adapter =  adaptadorLista

                return false
            }
        })

        // quando um item da lista e clicado
            findViewById<ListView>(R.id.lista).onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->

                    val inte = Intent(this, ecraVerReserva::class.java)
                    if (listaFiltradaAtuais.isEmpty()) {
                        inte.putExtra("idReserva", listaReservasAtuais[position].IDReserva.toString())
                        inte.putExtra("IDSala", listaReservasAtuais[position].IDSala.toString())
                        inte.putExtra("Estado", listaReservasAtuais[position].IDEstado.toString())
                        inte.putExtra("DataReserva", listaReservasAtuais[position].DataReserva.toString())
                        inte.putExtra("HoraInicio", listaReservasAtuais[position].HoraInicio.toString())
                        inte.putExtra("HoraFim", listaReservasAtuais[position].HoraFim.toString())
                        inte.putExtra("passa", "1")
                    }else {
                        inte.putExtra("idReserva", listaFiltradaAtuais[position].IDReserva.toString())
                        inte.putExtra("IDSala", listaFiltradaAtuais[position].IDSala.toString())
                        inte.putExtra("Estado", listaFiltradaAtuais[position].IDEstado.toString())
                        inte.putExtra("DataReserva", listaFiltradaAtuais[position].DataReserva.toString())
                        inte.putExtra("HoraInicio", listaFiltradaAtuais[position].HoraInicio.toString())
                        inte.putExtra("HoraFim", listaFiltradaAtuais[position].HoraFim.toString())
                        inte.putExtra("passa", "1")
                    }
                    startActivity(inte)
                }
    }

    fun carregarMenu(listaCentros : ArrayList<Centros>)
    {
        var drawerLayout = findViewById<DrawerLayout>(R.id.drawerlayout)
        var navigationView = findViewById<NavigationView>(R.id.nav_view)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout,R.string.open, R.string.close )
        drawerLayout.addDrawerListener(actionBarDrawerToggle )
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.menu.getItem(3).isChecked = true

        carregarCentros(navigationView,listaCentros)

        var header = navigationView.getHeaderView(0)
        var tvUsername = header.findViewById<TextView>(R.id.nomeUtilizador)


        var asset = assets(this)
        var url = "https://softinsa.herokuapp.com/utilizadores/get/".plus(asset.obterIDUser().toString())

        val requestQueue2 = Volley.newRequestQueue(this)
        val request2 = JsonObjectRequest(Request.Method.GET, url, null, {
                response ->try {
            // val jsonArray = response.getJSONArray("" + "user")
            val distr = response.getJSONObject("" + "data")
            tvUsername.setText(distr.getString("Pnome").toString()+" "+distr.getString("Unome").toString())

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })
        requestQueue2?.add(request2)
    }

    fun carregarCentros(navigationView: NavigationView, lista: ArrayList<Centros>)
    {
        var asset = assets(this)
        var spinner = navigationView.getHeaderView(0).findViewById<Spinner>(R.id.spinnerCentros)

        var url = "https://softinsa.herokuapp.com/utilizadores/pertence/".plus(asset.obterIDUser().toString())
        val requestQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET, url, null, {
                response ->try {
            val jsonArray = response.getJSONArray("" + "data")
            for (i in 0 until jsonArray.length()) {
                val distr = jsonArray.getJSONObject(i)
                lista.add(Centros(distr.getString("id").toInt(),distr.getString("Nome").toString()))
            }
            val adapter = adaptadorSpinner(
                this,
                android.R.layout.simple_spinner_item,
                lista
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinner.adapter = adapter
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })
        requestQueue?.add(request)

        val adapter = adaptadorSpinner(
            this,
            android.R.layout.simple_spinner_item,
            lista
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true

        return super.onOptionsItemSelected(item)
    }

    fun carregarDados(IDUser : String, IDCentro : String, Lista : ArrayList<Reservas>){
        var url = "https://softinsa.herokuapp.com/reservas/listReservasUserCentro/".plus(IDUser).plus("/").plus(IDCentro)
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try{
                val data = response.getJSONArray("" + "data")
                for (i in 0 until data.length()) {

                    if(!data.getString(i).equals("null"))
                   {
                        val distr = data.getJSONObject(i)
                        var reservasID=distr.optString("ReservasId")
                        var SalaId=distr.optString("SalaId")
                        var NomeSala=distr.optString("Nome")
                        var UtilizadoreId=distr.optString("UtilizadoreId")
                        var NomeReserva=distr.optString("NomeReserva")
                        var DataReserva=distr.optString("DataReserva")
                        var NumeroParticipantes=distr.optString("NumeroParticipantes")
                        var HoraInicio=distr.optString("HoraInicio")
                        var HoraFim=distr.optString("HoraFim")
                        var EstadoId=distr.optString("EstadoId")
                       Lista.add(Reservas(reservasID.toInt(), SalaId.toInt(), UtilizadoreId.toInt(),
                           NomeReserva, LocalDate.parse(DataReserva), NumeroParticipantes.toInt(),
                           LocalTime.parse(HoraInicio,DateTimeFormatter.ofPattern("HH:mm:ss")),
                           LocalTime.parse(HoraFim,DateTimeFormatter.ofPattern("HH:mm:ss")),
                           EstadoId.toInt(),NomeSala.toString()))                    }
                }
                findViewById<TextView>(R.id.aviso).visibility = TextView.VISIBLE;
                findViewById<TextView>(R.id.aviso).text = "A carregar ..."
                if (Lista.isEmpty()) {
                    findViewById<TextView>(R.id.aviso).visibility = TextView.VISIBLE;
                    findViewById<ListView>(R.id.lista).visibility = TextView.GONE;
                    findViewById<TextView>(R.id.aviso).text = "Não existem reservas para este centro"
                }
                else {
                    var adaptadorLista = adaptadorListaReservas(this, Lista)
                    findViewById<ListView>(R.id.lista).adapter = adaptadorLista
                    findViewById<TextView>(R.id.aviso).visibility = TextView.GONE;
                    findViewById<ListView>(R.id.lista).visibility = TextView.VISIBLE;
                }

            }catch (e: JSONException){
                e.printStackTrace()
            }
        }, {error -> error.printStackTrace()})
        queue.add(request)
    }
    fun CarregarItensLista(ReservasId:String,SalaId:String,UtilizadorId:String,NomeReserva:String,
                           DataReserva:String,NumeroParticipantes:String,HoraInicio:String,
                           HoraFim:String,EstadoId:String,Lista : ArrayList<Reservas>){

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        var urlSala = "https://softinsa.herokuapp.com/salas/get/".plus(SalaId.toString())
        val queueSala = Volley.newRequestQueue(this)
        val requestSala = JsonObjectRequest(Request.Method.GET, urlSala, null, { response ->

            try{
                val distr = response.getJSONObject("data")
                var nome= distr.getString("Nome").toString()
                //Toast.makeText(applicationContext,nome, Toast.LENGTH_SHORT).show()

                Lista.add(Reservas(ReservasId.toInt(), SalaId.toInt(), UtilizadorId.toInt(),
                    NomeReserva, LocalDate.parse(DataReserva), NumeroParticipantes.toInt(),
                    LocalTime.parse(HoraInicio,DateTimeFormatter.ofPattern("HH:mm:ss")),
                    LocalTime.parse(HoraFim,DateTimeFormatter.ofPattern("HH:mm:ss")),
                    EstadoId.toInt(),nome.toString()))

                var adaptadorLista = adaptadorListaReservas(this, Lista)
                findViewById<ListView>(R.id.lista).adapter = adaptadorLista
            }
            catch (e:JSONException){
                e.printStackTrace()
            }
        }, {error -> error.printStackTrace()})
        queueSala.add(requestSala)
    }
    fun refresh(){
        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
    }

}