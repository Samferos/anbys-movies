package iut.s4.sae.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    companion object {
        const val MOVIES_ARGUMENT = "movies"
        const val SEARCH_TERM_ARGUMENT = "search_term"
    }
}