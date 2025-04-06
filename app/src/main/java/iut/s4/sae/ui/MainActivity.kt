package iut.s4.sae.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
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
                R.id.bottom_bar_movies_menu -> {
                    TODO("Display trending movies fragment")
                }
                R.id.bottom_bar_favourite_movies_menu -> {
                    TODO("Display favourite movies fragment")
                }
                R.id.bottom_bar_settings_menu -> {
                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                    false
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val appBar = findViewById<Toolbar>(R.id.main_search_bar)
        appBar.inflateMenu(R.menu.search_bar_menu)
        appBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search_bar_advanced_search_menu -> {
                    TODO("Start advanced search activity")
                }
                R.id.search_bar_history_menu -> {
                    TODO("Start history activity")
                }
                R.id.search_bar_clear_history_menu -> {
                    TODO("Clear history")
                }
                else -> {
                    false
                }
            }
        }
        return super.onCreateOptionsMenu(menu)
    }
}