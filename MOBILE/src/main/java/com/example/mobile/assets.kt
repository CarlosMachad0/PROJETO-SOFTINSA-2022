package com.example.mobile
import android.content.Context
import android.util.Log
import android.widget.Toast

data class assets (var appContext : Context)
{

    var pref = appContext.getSharedPreferences("MyPref", 0)
    var editor = pref.edit()

    fun obterIDUser() : Int
    {
        return pref.getInt("IdUser", 0)
    }

    fun inserirIDUSer(IdUser : Int)
    {
        editor.putInt("IdUser",IdUser)
        editor.commit()

    }

    fun obterIDCentro() : Int
    {
        return pref.getInt("IdCentro", 0)
    }

    fun inserirIDCentro(IdCentro : Int)
    {
        editor.putInt("IdCentro",IdCentro)
        editor.apply()
    }

    fun obterNomeSala() : String?
    {
        return pref.getString("Nome","")
    }

    fun inserirNomeSala(NomeSala : String)
    {
        editor.putString("Nome",NomeSala)
        editor.apply()
    }

    fun obterToken() : String?
    {
        return pref.getString("token", null)
    }

    fun inserirToken(token : String)
    {
        editor.putString("token",token)
        editor.commit()
    }

    fun apagarDados()
    {
        editor.remove("IdUser");
        editor.remove("IdCentro");
        editor.remove("token");
        editor.commit();
    }
    fun apagaSala()
    {
        editor.remove("Nome");
    }
}