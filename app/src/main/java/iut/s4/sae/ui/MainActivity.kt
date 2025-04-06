package iut.s4.sae.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import iut.s4.sae.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val appBar = findViewById<Toolbar>(R.id.main_search_bar)
        setSupportActionBar(appBar)

        val bottomBar = findViewById<BottomNavigationView>(R.id.main_bottom_navbar)
        bottomBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_bar_settings_menu -> {
                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                    bottomBar.selectedItemId = R.id.bottom_bar_movies_menu
                    true
                }
                else -> {
                    true
                }
            }
        }
    }
}