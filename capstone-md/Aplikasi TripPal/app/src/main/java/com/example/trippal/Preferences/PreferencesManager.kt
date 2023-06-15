package com.example.trippal.Preferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private var prefManager: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    private val editPrefs = prefManager.edit()

    fun stringPreferences(key: String, value:String){
        editPrefs.putString(key, value)
            .apply()
    }

//    fun booleanPreferences(key: String, value: Boolean){
//        editPrefs.putBoolean(key, value)
//            .apply()
//    }

    fun clearPreference(){
        editPrefs.clear()
            .apply()
    }


    //auth
    val username = prefManager.getString(USERNAME, "")
    val email = prefManager.getString(USER_EMAIL,"")
    val token = prefManager.getString(TOKEN, "")

    //ui
    val photoProfile = prefManager.getString(PHOTO_PROFILE, "")
    val normalMode = prefManager.getBoolean(NORMAL_MODE, true)

    //aditional info
    val userHobby = prefManager.getString(HOBBY, "")
    val userReference = prefManager.getString(REFERENSI_WISATA, "")
    val userGender = prefManager.getString(GENDER, "")
    val age = prefManager.getString(AGE, "")


    companion object{
        //ui
        const val PREFERENCE_NAME = "my_pref"
        const val PHOTO_PROFILE = "photo_profile"
        const val NORMAL_MODE = "normal_mode"

        //aditional info
        const val HOBBY = "hobby"
        const val REFERENSI_WISATA = "referensi_wisata"
        const val GENDER = "gender"
        const val AGE = "age"

        //auth
        const val TOKEN = "user_token"
        const val USERNAME = "username"
        const val USER_EMAIL = "user_email"
    }
}