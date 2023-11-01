package com.example.asisten_damkar.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.asisten_damkar.response.LoginResponse
import com.example.asisten_damkar.view.HomeActivity
import com.example.asisten_damkar.view.LoginActivity

class LoginUtils {
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var con: Context
    val PRIVATE_MODE: Int = 0

    constructor(con: Context) {
        this.con = con
        pref = con.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    companion object {
        const val PREF_NAME = "LOGIN_SESSION"
        const val IS_LOGIN = "isLoggedIn"
        const val KEY_AT = "accessToken"
        const val ROLE = "role"
    }

    fun createLoginSession(loginResponse: LoginResponse) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(ROLE, loginResponse.tagRole)
        editor.putString(KEY_AT, loginResponse.key.accessToken.token)
        editor.commit()
    }

    fun getAccessToken(): String? {
        return pref.getString(KEY_AT, "")
    }

    fun getRole(): String? {
        return pref.getString(ROLE, "Error")
    }

    fun checkIsNotLogin() {
        if(!this.isLogin()) {
            val i = Intent(con, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            con.startActivity(i)
        }
    }

    fun checkIsLogin() {
        if(this.isLogin()) {
            val i = Intent(con, HomeActivity::class.java)
            con.startActivity(i)
        }
    }

    fun logOut() {
        editor.clear()
        editor.commit()
        val i = Intent(con, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        con.startActivity(i)
    }

    fun isLogin(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }
}