package com.example.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import org.json.JSONException
import android.widget.AdapterView.OnItemClickListener

class MainActivity : AppCompatActivity() {
    lateinit var actionBarDrawerToggle : ActionBarDrawerToggle

    override fun onResume() {
        super.onResume()

        var asset = assets(applicationContext)

        if (asset.obterIDUser() == 0)
            startActivity(Intent(this, ecraLogin::class.java))

        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.menu.getItem(2).isChecked = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.setTitle("Lista de espaços")

        var asset = assets(applicationContext)

        if (asset.obterIDUser() == 0)
            startActivity(Intent(this, ecraLogin::class.java))

        var listaEspacos = ArrayList<Sala>()
        var listaCentros = ArrayList<Centros>()
        var listaFiltrada = ArrayList<Sala>()
        var procurarEspacos = findViewById<SearchView>(R.id.searchView)

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
                    // nada aqui
                }

                R.id.reservas -> {
                    startActivity(Intent(this, ecraListaReservasAtuais::class.java))
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
                listaEspacos.clear()
                asset.inserirIDCentro(listaCentros[position].IDCentro)
                carregarEspacos(asset.obterIDCentro().toString(), listaEspacos)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        // quando um item e procurado na lista

        procurarEspacos.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {

                listaFiltrada.clear()

                for (espaco in listaEspacos)
                {
                    var numero = (espaco.Alocacao * espaco.Participantes) / 100
                    if (espaco.Nome.contains(query, ignoreCase = true) || numero.toString() == query)
                        listaFiltrada.add(espaco)
                }

                var adaptadorLista = adaptadorListaEspacos(applicationContext, listaFiltrada)
                findViewById<ListView>(R.id.lista).adapter =  adaptadorLista

                return false
            }
        })

        // quando um item da lista e clicado

        findViewById<ListView>(R.id.lista).onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                //Toast.makeText(applicationContext, listaEspacos[position].IDSala.toString(), Toast.LENGTH_LONG).show()

                val inte = Intent(this, ecraReserva::class.java)
                if (listaFiltrada.isEmpty())
                    inte.putExtra("idEspaco", listaEspacos[position].IDSala.toString())
                else
                    inte.putExtra("idEspaco", listaFiltrada[position].IDSala.toString())
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

        navigationView.menu.getItem(2).isChecked = true

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

    fun carregarEspacos(idCentro : String, lista : ArrayList<Sala>)
    {
        var url = "https://softinsa.herokuapp.com/salas/centro/".plus(idCentro)
        val requestQueue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url, null, {
                response ->try {

            val jsonArray = response.getJSONArray("" + "data")
            for (i in 0 until jsonArray.length()) {
                val distr = jsonArray.getJSONObject(i)
                lista.add(Sala(distr.getString("id").toInt(),
                    distr.getString("Capacidade").toInt(),
                    distr.getString("Alocacao").toInt(),
                    distr.getString("Nome").toString()))
            }

            findViewById<TextView>(R.id.aviso).visibility = TextView.VISIBLE;
            findViewById<TextView>(R.id.aviso).text = "A carregar ..."

            if (lista.isEmpty()) {
                findViewById<TextView>(R.id.aviso).visibility = TextView.VISIBLE;
                findViewById<ListView>(R.id.lista).visibility = TextView.GONE;
                findViewById<TextView>(R.id.aviso).text = "Não existem salas para este centro"
            }
            else {
                var adaptadorLista = adaptadorListaEspacos(this, lista)
                findViewById<ListView>(R.id.lista).adapter = adaptadorLista
                findViewById<TextView>(R.id.aviso).visibility = TextView.GONE;
                findViewById<ListView>(R.id.lista).visibility = TextView.VISIBLE;
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })

        requestQueue.add(request)
    }
}