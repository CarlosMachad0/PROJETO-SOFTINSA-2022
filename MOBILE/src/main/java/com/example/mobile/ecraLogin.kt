package com.example.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.util.HashMap

class ecraLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecra_login)

        var btnEntrar = findViewById<Button>(R.id.btnEntrar)
        var email = findViewById<TextInputEditText>(R.id.email)
        var password = findViewById<TextInputEditText>(R.id.password)

        btnEntrar.setOnClickListener {
            if (email.text.toString() != "" && password.text.toString() != "")
                logIn(email.text.toString(), password.text.toString())
            else
                Toast.makeText(this,"Preencha todos os campos",Toast.LENGTH_SHORT).show()
        }

    }

    fun logIn(email : String, password : String)
    {
        var url = "https://softinsa.herokuapp.com/auth/loginRequesitante"
        val queue = Volley.newRequestQueue(this)
        var asset = assets(this)

        val request = object : StringRequest( Method.POST, url, Response.Listener { response ->
            //Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()

            val data = JSONObject(response)
            val message: String = data.optString("message")
            val sucesso: String = data.optString("sucesso")

            if (sucesso == "true")
            {
                val id = data.optString("id")
                val login = data.optString("login")
                val token = data.optString("token")

                if (login == "1") { // primeiro login
                    var inte = Intent(this, ecraMudarPassword::class.java)
                    inte.putExtra("IdUser",id)
                    inte.putExtra("token",token)
                    startActivity(inte)
                }
                else{
                    asset.inserirIDUSer(id.toInt())
                    asset.inserirToken(token)
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
            else
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        }, Response.ErrorListener { // displaying toast message on response failure.

        }) {
            override fun getParams(): Map<String, String>? {

                val params: MutableMap<String, String> = HashMap()

                params["email"] = email
                params["password"] = password

                return params
            }
        }
        queue.add(request)
    }
}