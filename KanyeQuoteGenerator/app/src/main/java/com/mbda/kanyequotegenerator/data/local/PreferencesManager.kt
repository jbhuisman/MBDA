package com.mbda.kanyequotegenerator.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("QuotePrefs", Context.MODE_PRIVATE)

    fun saveLastAuthor(author: String) {
        prefs.edit().putString("LAST_AUTHOR", author).apply()
    }

    fun getLastAuthor(): String {
        return prefs.getString("LAST_AUTHOR", "") ?: ""
    }

    fun saveProfileImageUri(uriString: String) {
        prefs.edit().putString("PROFILE_IMAGE_URI", uriString).apply()
    }

    fun getProfileImageUri(): String? {
        return prefs.getString("PROFILE_IMAGE_URI", null)
    }

    fun saveScore(score: Int) {
        prefs.edit().putInt("GAME_SCORE", score).apply()
    }

    fun getScore(): Int {
        return prefs.getInt("GAME_SCORE", 0)
    }
}