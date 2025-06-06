package iut.s4.sae.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import iut.s4.sae.R
import iut.s4.sae.SettingsManager
import iut.s4.sae.ui.fragment.SettingsFragment

class SettingsActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val appbar = findViewById<Toolbar>(R.id.settings_toolbar)
        setSupportActionBar(appbar)

        appbar.setNavigationOnClickListener {
            finish()
        }

        if (savedInstanceState == null) {
            val fragment = SettingsFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.settings_fragment_view, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val lang = SettingsManager.getPreferredLanguage(newBase)
        super.attachBaseContext(LanguageContextWrapper.wrap(newBase, lang))
    }

}