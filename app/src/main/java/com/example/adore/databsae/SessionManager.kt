package com.example.adore.databsae

import android.content.Context
import android.content.SharedPreferences

class SessionManager(val context: Context) {
    companion object{
        const val IS_LOGIN = "isLoggedIn"
        const val KEY_NAME = "name"
        const val KEY_MOBILE_NUMBER = "mobileNumber"
        const val KEY_PASSWORD = "password"
        const val KEY_USER_ID = "userId"
    }
    private val userSession: SharedPreferences = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = userSession.edit()

    fun createLoginSession(name: String, mobileNo: String, password: String, userId:Long){
        editor.apply {
            putBoolean(IS_LOGIN, true)
            putString(KEY_NAME, name)
            putString(KEY_MOBILE_NUMBER, mobileNo)
            putString(KEY_PASSWORD, password)
            putLong(KEY_USER_ID, userId)
            commit()
        }
    }

    fun getUserId() = userSession.getLong(KEY_USER_ID, 1L)

    fun getUserDetailsFromSession(): Map<String, *> = userSession.all
    fun checkLogin() = userSession.getBoolean(IS_LOGIN, false)
    fun logoutUserFromSession(){
        editor.clear()
        editor.commit()
    }
}