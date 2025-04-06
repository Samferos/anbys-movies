package iut.s4.sae.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import iut.s4.sae.R
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val searchBar = findViewById<SearchBar>(R.id.main_search_bar)
        setSupportActionBar(searchBar)

        val searchView = findViewById<SearchView>(R.id.main_search_view)

        searchBar.setNavigationOnClickListener {
            if (searchView.text.isEmpty()) {
                searchView.show()
            }
            else {
                // Search with already present text
            }
        }

        searchView
            .editText
            .setOnEditorActionListener { v, actionId, event ->
                if (v.text.isEmpty()) {
                    val random = Random.nextInt() % 90
                    if (random == 6) {
                        startActivity(Intent.parseUri("https://www.youtube.com/watch?v=E4WlUXrJgy4", 0))
                        Toast.makeText(this@MainActivity, "LOL GET RICKROLLED", Toast.LENGTH_SHORT).show()
                        return@setOnEditorActionListener true
                    }
                    Toast.makeText(this@MainActivity, getString(R.string.warning_invalid_search), Toast.LENGTH_SHORT).show()
                    return@setOnEditorActionListener true
                }
                searchBar.setText(v.text)
                searchView.hide()
                false
            }

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
        val searchBar = findViewById<Toolbar>(R.id.main_search_bar)
        searchBar.inflateMenu(R.menu.search_bar_menu)
        searchBar.setOnMenuItemClickListener {
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