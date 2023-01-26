package com.salim.mypokedex.utilities

import android.content.SharedPreferences

/***
 * Wrapper class to make testing caching easier. Remove any android stuff from testing.
 *
 * @constructor construct wrapper by passing in [SharedPreferences]
 */
class SharedPreferencesWrapper(private val sharedPreferences: SharedPreferences) {

    fun saveString(key: String, value: String) {
        sharedPreferences.edit().apply {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }
}
