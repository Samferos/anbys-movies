package iut.s4.sae.ui

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import iut.s4.sae.R
import androidx.core.content.edit
import iut.s4.sae.SettingsManager

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val languagePreference: ListPreference? = findPreference("language_preference")
        languagePreference?.setOnPreferenceChangeListener { _, newValue ->
            val lang = newValue.toString()

            // Save the new language
            SettingsManager.setPreferredLanguage(requireContext(), lang)

            // Optional: show a toast
            Toast.makeText(requireContext(), getString(R.string.reload_for_language_changes), Toast.LENGTH_SHORT).show()

            // Restart the app with a clean stack
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            startActivity(intent)

            // Smooth transition
            activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            true
        }



    }


}