package com.example.tablet

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.HashMap

class Login : AppCompatActivity() {



    lateinit var pref : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    override fun onResume() {
        super.onResume()

        pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        editor = pref.edit()

        editor.remove("idUser"); // will delete key name
        editor.remove("idCentro"); // will delete key name
        editor.commit(); // commit changes
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)



        pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        editor = pref.edit()




        val imageButton10 = findViewById<ImageButton>(R.id.imageButton5)
        imageButton10.setOnClickListener { view ->
            val intent = Intent (this, inicio::class.java)
            startActivity(intent)
        }




            val button = findViewById<Button>(R.id.button)
            button.setOnClickListener {

                entrar()


            }

    }

    fun entrar()
    {
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password_toggle).text.toString()



        val url = "https://softinsa.herokuapp.com/auth/loginGestor"
        val queue = Volley.newRequestQueue(this)

        val request = object : StringRequest( Method.POST, url, Response.Listener { response ->


            val data = JSONObject(response)
            val message: String = data.optString("message")
            val sucesso: String = data.optString("sucesso")

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


            if (sucesso.contains("true"))
            {


                val id = data.optString("id")
                val login = data.optString("login")
                val token = data.optString("token")







                val intent = Intent (this, Definicoes::class.java)
                startActivity(intent)


            }

        }, Response.ErrorListener {

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