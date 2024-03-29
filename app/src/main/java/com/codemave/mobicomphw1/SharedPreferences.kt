package com.codemave.mobicomphw1

import android.content.Context

class SharedPreferences(context: Context) {
    private val sharedPref = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

    var username: String
        get() = sharedPref.getString("username","") ?: ""
        set(value) = sharedPref.edit().putString("username", value).apply()

    var password: String
        get() = sharedPref.getString("password","") ?: ""
        set(value) = sharedPref.edit().putString("password", value).apply()

    var photoUrl: String
        get() = sharedPref.getString("photoUrl","") ?: ""
        set(value) = sharedPref.edit().putString("photoUrl", value).apply()
}