package iut.s4.sae.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.SwitchPreference
import com.google.android.material.materialswitch.MaterialSwitch
import iut.s4.sae.R

class SettingsActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val appbar = findViewById<Toolbar>(R.id.settings_toolbar)
        setSupportActionBar(appbar)

        appbar.setNavigationOnClickListener {
            finish()
        }
    }
}