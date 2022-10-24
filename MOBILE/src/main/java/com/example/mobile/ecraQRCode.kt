package com.example.mobile

import android.app.SearchManager
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ecraQRCode : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecra_qrcode)

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        var btnVoltar = findViewById<Button>(R.id.btnVoltar)

        setupPermissions()

        codeScanner = CodeScanner(this, scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {

                try {
                    val id= JSONObject(it.text)
                    val valor= id.get("id")

                    // Toast.makeText(this, valor.toString(),Toast.LENGTH_SHORT).show()

                    if (valor.toString().toDoubleOrNull() != null)
                    {
                        var asset = assets(this)
                        var url = "https://softinsa.herokuapp.com/salas/utilizador/".plus(asset.obterIDUser()).plus("/").plus(valor)

                        val requestQueue = Volley.newRequestQueue(this)
                        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->try {

                            if (!response.optString("dados").toBoolean())
                                Toast.makeText(this, "Não tem permissao para reservar esta sala", Toast.LENGTH_LONG).show()

                            if (response.optString("dados").toBoolean())
                                rederecionar(valor.toString())

                        } catch (e: JSONException) { e.printStackTrace() }
                        }, { error -> error.printStackTrace() })
                        requestQueue.add(request)
                    }
                    else
                        Toast.makeText(this, "Erro", Toast.LENGTH_LONG).show()



                }
                catch (e : JSONException)
                {
                    Toast.makeText(this,"Ocorreu um erro ao ler o qrcode", Toast.LENGTH_LONG).show()
                }
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "Erro: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        btnVoltar.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    fun setupPermissions()
    {
        val permission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED)
            makeRequest()
    }

    fun makeRequest()
    {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 101)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            101 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permissao negada", Toast.LENGTH_LONG).show()

            }
        }
    }

    fun rederecionar(idEspaco : String)
    {
        startActivity(Intent(this, ecraReserva::class.java).putExtra("idEspaco", idEspaco))
    }
}