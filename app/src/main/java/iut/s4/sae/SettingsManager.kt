package iut.s4.sae


import android.content.Context
import androidx.preference.PreferenceManager
import androidx.core.content.edit


/**
 * Singleton object responsible for managing app-wide user settings stored in SharedPreferences.
 * This includes user preferences for adult content visibility and preferred language.
 */
object SettingsManager {

    /**
     * Checks if the user has allowed adult content to be shown.
     *
     * @param context Context used to access SharedPreferences.
     * @return True if adult content is allowed, false otherwise.
     */
    fun isAdultContentAllowed(context: Context): Boolean {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPref.getBoolean("allow_adult_content_preference", false)
    }

    /**
     * Retrieves the user's preferred language setting.
     *
     * @param context Context used to access SharedPreferences.
     * @return The language code (e.g., "en", "fr"). Defaults to "fr" if not set.
     */
    fun getPreferredLanguage(context: Context): String {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPref.getString("language_preference", "fr") ?: "fr"
    }

    /**
     * Updates the user's preferred language setting.
     *
     * @param context Context used to access SharedPreferences.
     * @param language New language code to save (e.g., "en", "fr").
     */
    fun setPreferredLanguage(context: Context, language: String) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPref.edit {
            putString("language_preference", language)
        }
    }
}
