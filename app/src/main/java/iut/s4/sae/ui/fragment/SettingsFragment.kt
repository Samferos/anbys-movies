package iut.s4.sae.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import iut.s4.sae.R
import iut.s4.sae.SettingsManager
import iut.s4.sae.ui.MainActivity

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                activity?.overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, android.R.anim.fade_in, android.R.anim.fade_out)
            }

            true
        }
        val aboutPreference : Preference? = findPreference("about_dialog")

        aboutPreference?.setOnPreferenceClickListener {
            context?.let { it1 ->
                MaterialAlertDialogBuilder(it1)
                    .setTitle(resources.getString(R.string.app_name))
                    .setIcon(resources.getDrawable(R.drawable.ic_launcher_foreground))
                    .setMessage(resources.getString(R.string.content_dialog))
                    .setPositiveButton(resources.getString(R.string.close_dialog)) { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
            }
            true
        }
    }


}