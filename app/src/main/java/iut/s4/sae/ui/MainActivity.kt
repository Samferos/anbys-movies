package iut.s4.sae.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import iut.s4.sae.R
import iut.s4.sae.SettingsManager
import iut.s4.sae.action.ACTION_SEARCH_BY_MOVIE
import iut.s4.sae.model.Movies
import iut.s4.sae.ui.fragment.FavoriteMoviesFragment
import iut.s4.sae.ui.fragment.MainViewFragmentFactory
import iut.s4.sae.ui.fragment.TrendingMoviesFragment
import iut.s4.sae.ui.viewmodel.TrendingMoviesViewModel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    val allowAdult : Boolean
        get() = SettingsManager.isAdultContentAllowed(this)
    val language : String
        get() = SettingsManager.getPreferredLanguage(this)

    private val trendingMoviesViewModel : TrendingMoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = MainViewFragmentFactory(trendingMoviesViewModel)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val searchBar = findViewById<SearchBar>(R.id.main_search_bar)
        setSupportActionBar(searchBar)

        val searchView = findViewById<SearchView>(R.id.main_search_view)

        // Set fragment to trending movies by default if activity was newly created
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.main_movie_list_fragment_view, TrendingMoviesFragment(trendingMoviesViewModel)
                )
            }
        }

        searchBar.setNavigationOnClickListener {
            if (searchView.text.isEmpty()) {
                searchView.show()
            }
            else {
                searchMovie(searchView.text.toString())
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
                searchMovie(v.text.toString())
                searchView.clearFocus()
                true
            }

        val bottomBar = findViewById<BottomNavigationView>(R.id.main_bottom_navbar)
        bottomBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_bar_movies_menu -> {
                    if (supportFragmentManager.findFragmentById(R.id.main_movie_list_fragment_view) is TrendingMoviesFragment)
                        return@setOnItemSelectedListener false
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.main_movie_list_fragment_view, TrendingMoviesFragment(trendingMoviesViewModel))
                    }
                    true
                }
                R.id.bottom_bar_favourite_movies_menu -> {
                    supportFragmentManager.commit {
                        if (supportFragmentManager.findFragmentById(R.id.main_movie_list_fragment_view) is FavoriteMoviesFragment)
                            return@setOnItemSelectedListener false
                        setReorderingAllowed(true)
                        replace(
                            R.id.main_movie_list_fragment_view,
                            FavoriteMoviesFragment.newInstance(
                                getFavoriteMovie(this@MainActivity)
                            )
                        )
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

    override fun attachBaseContext(newBase: Context) {
        val lang = SettingsManager.getPreferredLanguage(newBase)
        super.attachBaseContext(LanguageContextWrapper.wrap(newBase, lang))
    }

    fun getFavoriteMovie(context : Context) : Movies {
        val sharedPreferences : SharedPreferences = context.getSharedPreferences("favorites", MODE_PRIVATE)
        val jsonMovies = sharedPreferences.getString("favorite_movies",null)
        return if (jsonMovies != null) {
            Json.decodeFromString<Movies>(jsonMovies)
        } else {
            Movies(mutableListOf())
        }
    }

    private fun searchMovie(searchTerm: String) {
        val intent = Intent(this, SearchActivity::class.java)
        intent
            .putExtra(SearchActivity.EXTRA_SEARCH, searchTerm)
            .action = ACTION_SEARCH_BY_MOVIE
        startActivity(intent)
    }
}