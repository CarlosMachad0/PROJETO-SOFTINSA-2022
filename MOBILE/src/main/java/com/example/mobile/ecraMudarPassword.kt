package com.example.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.util.HashMap

class ecraMudarPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecra_mudar_password)

        var idUser : String
        var token  : String

        var asset = assets(this)

        var btnMudar = findViewById<Button>(R.id.btnAlterar)
        var passwordAntiga = findViewById<TextInputEditText>(R.id.passwordAntiga)
        var passwordNova = findViewById<TextInputEditText>(R.id.passwordNova)
        var passwordNovaConfirmar = findViewById<TextInputEditText>(R.id.passwordNovaConfirmar)
        var btnVoltar = findViewById<Button>(R.id.voltar)


        if (intent.getStringExtra("IdUser").isNullOrEmpty()) {
            idUser = asset.obterIDUser().toString()
            token = asset.obterToken().toString()
            btnVoltar.visibility = Button.VISIBLE
        }
        else {
            idUser = intent.getStringExtra("IdUser").toString()
            token = intent.getStringExtra("token").toString()
        }

        // Toast.makeText(this, idUser, Toast.LENGTH_SHORT).show()


        btnVoltar.setOnClickListener {
            finish()
        }

        btnMudar.setOnClickListener {
            if (passwordAntiga.text.toString().isNotEmpty() && passwordNova.text.toString().isNotEmpty() && passwordNovaConfirmar.text.toString().isNotEmpty())
                mudarPassword(passwordAntiga.text.toString(), passwordNova.text.toString(), passwordNovaConfirmar.text.toString(), idUser,token)
            else
                Toast.makeText(this,"Preencha todos os campos",Toast.LENGTH_SHORT).show()
        }
    }

    fun mudarPassword(passwordAntiga : String, passwordNova : String, passwordNovaConfrimar : String, idUser : String, token : String)
    {
        if (passwordNova != passwordNovaConfrimar)
            Toast.makeText(this, "As passwords nao coincidem", Toast.LENGTH_SHORT).show()
        else
        {
            var url = "https://softinsa.herokuapp.com/utilizadores/editpasse/".plus(idUser)
            val queue = Volley.newRequestQueue(this)

            var asset = assets(this)

            val request = object : StringRequest( Method.PUT, url, Response.Listener { response ->

                val data = JSONObject(response)
                val message: String = data.optString("message")
                val sucesso: String = data.optString("sucesso")

                if (sucesso == "true") {
                    asset.inserirIDUSer(idUser.toInt())
                    asset.inserirToken(token)
                    startActivity(Intent(this, MainActivity::class.java))
                }
                else
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            }, Response.ErrorListener {

            }) {
                override fun getParams(): Map<String, String>? {

                    val params: MutableMap<String, String> = HashMap()

                    params["PalavraPasseNova"] = passwordNova
                    params["PalavraPasseAntiga"] = passwordAntiga

                    return params
                }
            }
            queue.add(request)
        }

    }
}