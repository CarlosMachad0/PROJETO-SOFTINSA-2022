package com.example.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.NavUtils
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import org.json.JSONException
import java.util.ArrayList

class ecraPerfil : AppCompatActivity() {
    lateinit var actionBarDrawerToggle : ActionBarDrawerToggle
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true

        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()

        var asset = assets(applicationContext)
        if (asset.obterIDUser() == 0)
            startActivity(Intent(this, ecraLogin::class.java))

        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.menu.getItem(0).isChecked = true

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecra_perfil)

        var asset = assets(applicationContext)
        if (asset.obterIDUser() == 0)
            startActivity(Intent(this, ecraLogin::class.java))
        var listaCentros = ArrayList<Centros>()

        getUtilizador()

        //Ativity alterar palavra pass
        var text_alterarpass = findViewById<TextView>(R.id.txt_alterarpassword)
        text_alterarpass.setOnClickListener {
            startActivity(Intent(this, ecraMudarPassword::class.java))
        }
        //Ativity voltar
        var btn_Voltar = findViewById<TextView>(R.id.btn_voltar)
        btn_Voltar.setOnClickListener {
            finish()
        }
        // carregar menu lateral
        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        carregarMenu(listaCentros)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.perfil -> {
                    //nada aqui
                }
                R.id.qrcode -> {
                    startActivity(Intent(this, ecraQRCode::class.java))
                }
                R.id.espacos -> {
                    startActivity(Intent(this, MainActivity::class.java))
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

    }
    fun getUtilizador()
    {
        var nomeUtilizador = findViewById<EditText>(R.id.txt_Nome)
        var emailUtilizador = findViewById<TextView>(R.id.txt_Email)
        var asset = assets(this)
        var url = "https://softinsa.herokuapp.com/utilizadores/get/".plus(asset.obterIDUser())

        val requestQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val utilizador = response.getJSONObject("data")
                nomeUtilizador.setText(utilizador.getString("Pnome") + " " + utilizador.getString("Unome"))
                emailUtilizador.text = utilizador.getString("Email").toString()

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, { error -> error.printStackTrace() })
        requestQueue?.add(request)
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
}