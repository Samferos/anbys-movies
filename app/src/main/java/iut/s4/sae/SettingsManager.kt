package iut.s4.sae


import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.core.content.edit

object SettingsManager {

    fun isAdultContentAllowed(context: Context): Boolean {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPref.getBoolean("allow_adult_content_preference", false)
    }

    fun getPreferredLanguage(context: Context): String {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPref.getString("language_preference", "fr") ?: "fr"
    }

    fun setPreferredLanguage(context: Context, language: String) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPref.edit() { putString("language_preference", language) }
    }

}