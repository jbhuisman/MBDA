package com.mbda.kanyequotegenerator.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveLastAuthor(author: String) {
        sharedPreferences.edit().putString("last_author", author).apply()
    }

    fun getLastAuthor(): String {
        return sharedPreferences.getString("last_author", "Kanye West") ?: "Kanye West"
    }
}