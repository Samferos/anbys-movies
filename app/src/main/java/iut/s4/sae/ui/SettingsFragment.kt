package iut.s4.sae.ui

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import iut.s4.sae.R
import androidx.core.content.edit

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val languagePreference: ListPreference? = findPreference("language_preference")
        languagePreference?.setOnPreferenceChangeListener { _, newValue ->
            val lang = newValue.toString()
            val sharedPreferences = requireContext().getSharedPreferences("language", MODE_PRIVATE)
            sharedPreferences.edit {
                putString("language_preference", lang)
            }
            Toast.makeText(requireContext(), getString(R.string.reload_for_language_changes), Toast.LENGTH_LONG).show()
            activity?.recreate()
            true
        }
    }


}