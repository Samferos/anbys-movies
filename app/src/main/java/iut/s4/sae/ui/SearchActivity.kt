package iut.s4.sae.ui

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import iut.s4.sae.R
import iut.s4.sae.SettingsManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchActivity : AppCompatActivity() {

    private val viewModel : SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        val appbar = findViewById<Toolbar>(R.id.search_app_bar)
        setSupportActionBar(appbar)

        appbar.setNavigationOnClickListener {
            finish()
        }

        val filterChipGroup = findViewById<ChipGroup>(R.id.search_chip_group)
        val noMoviesFoundText = findViewById<TextView>(R.id.search_no_movies_found)

        val genreId = intent.getIntExtra(EXTRA_GENREID, 0)
        val searchText = intent.getStringExtra(EXTRA_SEARCH)

        val language = SettingsManager.getPreferredLanguage(this@SearchActivity)
        val allowAdult = SettingsManager.isAdultContentAllowed(this@SearchActivity)

        viewModel.apply {
            includeAdult = allowAdult
            this.language = language
        }

        appbar.title = searchText

        val results = findViewById<RecyclerView>(R.id.search_results_view)

        val resultsLayoutManager = LinearLayoutManager(this)
        val resultsMoviesAdapter = FavoriteMoviesAdapter {
            position ->
            val id = viewModel.moviesResults.results[position].id
            val intent = Intent(this, MovieDetailActivity::class.java)
                .putExtra("movie_id", id)
            startActivity(intent)
        }

        // --- Movies Results (RecyclerView) setup
        results.layoutManager = resultsLayoutManager
        results.adapter = resultsMoviesAdapter
        results.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var lastVisibleItem : Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.d(this@SearchActivity::class.simpleName,"Last visible item position : ${resultsLayoutManager.findFirstVisibleItemPosition()} -- ${resultsLayoutManager.findLastVisibleItemPosition()}")
                if (resultsLayoutManager.findLastVisibleItemPosition() == lastVisibleItem) return
                if (resultsLayoutManager.findLastVisibleItemPosition() >= viewModel.moviesResults.results.size - 1) {
                    Log.d(this@SearchActivity::class.simpleName, "Fetching next movie page")
                    viewModel.nextPage()
                }
                lastVisibleItem = resultsLayoutManager.findLastVisibleItemPosition()
            }
        })

        lifecycleScope.launch {
            viewModel.searchMovies(searchText ?: "").join() // Only start collecting, AFTER we have initial movies
            findViewById<View>(R.id.search_results_loading).visibility = View.GONE
            if (viewModel.moviesResults.results.isEmpty()) { // If the initial search provided no results.
                filterChipGroup.visibility = View.GONE
                noMoviesFoundText.visibility = View.VISIBLE
                return@launch
            }
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newMoviesFlow.collect {
                    Log.d(this::class.simpleName, "Updating Movies (${it.results.size}) : ${it.results}")
                    Log.d(this::class.simpleName, "Current Movies (${viewModel.moviesResults.results.size}) : ${viewModel.moviesResults.results}")
                    resultsMoviesAdapter.addMovies(it)
                }
            }
        }
    }


    override fun attachBaseContext(newBase: Context) {
        val sharedPreferences = newBase.getSharedPreferences("language", MODE_PRIVATE)
        val lang = sharedPreferences.getString("language_preference", "") ?: "en"
        super.attachBaseContext(LanguageContextWrapper.wrap(newBase, lang))
    }

    companion object {
        const val EXTRA_SEARCH = "iut.s4.sae.EXTRA_SEARCH"
        const val EXTRA_GENREID = "iut.s4.sae.EXTRA_GENREID"
    }
}