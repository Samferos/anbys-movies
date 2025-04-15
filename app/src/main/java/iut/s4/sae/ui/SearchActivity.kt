package iut.s4.sae.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import iut.s4.sae.R
import iut.s4.sae.model.Movies

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        val appbar = findViewById<Toolbar>(R.id.search_app_bar)
        setSupportActionBar(appbar)

        appbar.setNavigationOnClickListener {
            finish()
        }

        val searchTerm = intent.getStringExtra(SEARCH_TERM_ARGUMENT) ?: ""

        appbar.title = searchTerm

        val movieResults = intent.getParcelableExtra<Movies>(MOVIES_ARGUMENT) ?: Movies(listOf())

        if (movieResults.results.isEmpty()) {
            val filters = findViewById<ChipGroup>(R.id.search_chip_group)
            filters.visibility = View.GONE
            val noResultsTextView = findViewById<TextView>(R.id.search_no_movies_found)
            noResultsTextView.visibility = View.VISIBLE
            return
        }
        // Nothing runs after the previous if statement, if no movies are found.

        val results = findViewById<RecyclerView>(R.id.search_results_view)

        results.adapter = FavoriteMoviesAdapter(movieResults) {
            position ->
            val id = movieResults.results[position].id
            val intent = Intent(this, MovieDetailActivity::class.java)
                .putExtra("movie_id", id)
            startActivity(intent)
        }
        results.layoutManager = LinearLayoutManager(this)
    }

    override fun attachBaseContext(newBase: Context) {
        val sharedPreferences = newBase.getSharedPreferences("language", MODE_PRIVATE)
        val lang = sharedPreferences.getString("language_preference", "") ?: "en"
        super.attachBaseContext(LanguageContextWrapper.wrap(newBase, lang))
    }

    companion object {
        const val MOVIES_ARGUMENT = "movies"
        const val SEARCH_TERM_ARGUMENT = "search_term"
    }
}