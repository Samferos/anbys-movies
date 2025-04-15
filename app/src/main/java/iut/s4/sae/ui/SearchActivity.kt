package iut.s4.sae.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import iut.s4.sae.R
import iut.s4.sae.SettingsManager
import iut.s4.sae.network.MovieDao
import kotlinx.coroutines.runBlocking

class SearchActivity : AppCompatActivity() {

    var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        val appbar = findViewById<Toolbar>(R.id.search_app_bar)
        setSupportActionBar(appbar)

        appbar.setNavigationOnClickListener {
            finish()
        }

        val genreId = intent.getIntExtra("genre_id", -1)
        val genreName = intent.getStringExtra("genre_name")
        val searchTerm = intent.getStringExtra(SEARCH_TERM_ARGUMENT)

        val movieResults = runBlocking {
            val language = SettingsManager.getPreferredLanguage(this@SearchActivity)
            val allowAdult = SettingsManager.isAdultContentAllowed(this@SearchActivity)

            when {
                genreId != -1 -> {
                    appbar.title=genreName
                    MovieDao.getInstance().discoverByGenre(genreId, language=language, includeAdult = allowAdult)
                }
                !searchTerm.isNullOrBlank() -> {
                    appbar.title = searchTerm
                    Log.d("test",searchTerm)
                    MovieDao.getInstance().searchMovies(searchTerm, currentPage, language, allowAdult)
                }
                else -> {
                    // fallback, nothing to show
                    null
                }
            }
        }
        if (movieResults == null || movieResults.results.isEmpty()) {
            findViewById<ChipGroup>(R.id.search_chip_group).visibility = View.GONE
            findViewById<TextView>(R.id.search_no_movies_found).visibility = View.VISIBLE
            return
        }

        val results = findViewById<RecyclerView>(R.id.search_results_view)
        results.layoutManager = LinearLayoutManager(this)
        results.adapter = FavoriteMoviesAdapter(movieResults) { position ->
            val id = movieResults.results[position].id
            val intent = Intent(this, MovieDetailActivity::class.java)
                .putExtra("movie_id", id)
            startActivity(intent)
        }
    }


    override fun attachBaseContext(newBase: Context) {
        val sharedPreferences = newBase.getSharedPreferences("language", MODE_PRIVATE)
        val lang = sharedPreferences.getString("language_preference", "") ?: "en"
        super.attachBaseContext(LanguageContextWrapper.wrap(newBase, lang))
    }

    companion object {
        const val SEARCH_TERM_ARGUMENT = "search_term"
    }
}