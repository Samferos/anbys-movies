package iut.s4.sae.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import iut.s4.sae.R
import iut.s4.sae.model.Movies
import iut.s4.sae.network.MovieDao
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val searchBar = findViewById<SearchBar>(R.id.main_search_bar)
        setSupportActionBar(searchBar)

        val searchView = findViewById<SearchView>(R.id.main_search_view)

        // Set fragment to trending movies by default
        supportFragmentManager.commit {
            runBlocking {
                replace(R.id.main_movie_list_fragment_view, TrendingMoviesFragment.newInstance(
                    MovieDao.getInstance().fetchTrendingMovies(), MovieDao.getInstance().fetchGenres()))
            }
        }

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
                    supportFragmentManager.commit {
                        runBlocking {
                            replace(R.id.main_movie_list_fragment_view, TrendingMoviesFragment.newInstance(
                                MovieDao.getInstance().fetchTrendingMovies(), MovieDao.getInstance().fetchGenres()))
                        }
                    }
                    true
                }
                R.id.bottom_bar_favourite_movies_menu -> {
                    supportFragmentManager.commit {
                        runBlocking {
                            replace(
                                R.id.main_movie_list_fragment_view,
                                FavoriteMoviesFragment.newInstance(
                                    getFavoriteMovie(this@MainActivity)
                                )
                            )
                        }
                    }
                    true
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

    fun getFavoriteMovie(context : Context) : Movies {
        val sharedPreferences : SharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val jsonMovies = sharedPreferences.getString("favorite_movies",null)
        return if (jsonMovies != null) {
            Json.decodeFromString<Movies>(jsonMovies)
        } else {
            Movies(listOf())
        }
    }
}