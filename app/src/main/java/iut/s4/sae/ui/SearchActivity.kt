package iut.s4.sae.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iut.s4.sae.R
import iut.s4.sae.SettingsManager
import iut.s4.sae.action.*
import iut.s4.sae.ui.adapter.MovieEntriesAdapter
import iut.s4.sae.ui.fragment.SearchFilterBottomSheetFragment
import iut.s4.sae.ui.viewmodel.SearchViewModel

/**
 * An activity that allow to search for movies.
 * It has two main ways of searching movies :
 *
 * It can search by [ACTION_SEARCH_BY_MOVIE], which
 * will simply search by title with the given [EXTRA_SEARCH]
 * intent extra.
 *
 * Or it can search by [ACTION_SEARCH_BY_GENRE] by passing the
 * ID through the intent extra [EXTRA_GENRE_ID].
 */
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

        val noMoviesFoundText = findViewById<TextView>(R.id.search_no_movies_found)

        val genreId = intent.getIntExtra(EXTRA_GENRE_ID, 0)
        val searchText = intent.getStringExtra(EXTRA_SEARCH) ?: ""

        val language = SettingsManager.getPreferredLanguage(this@SearchActivity)
        val allowAdult = SettingsManager.isAdultContentAllowed(this@SearchActivity)

        viewModel.setSearchSettings(language, allowAdult)

        appbar.title = searchText

        // --- Movies Results (RecyclerView) setup
        val results = findViewById<RecyclerView>(R.id.search_results_view)

        val resultsLayoutManager = LinearLayoutManager(this)
        val resultsMoviesAdapter = MovieEntriesAdapter(viewModel.moviesResults) {
            position ->
            val id = viewModel.moviesResults.results[position].id
            val intent = Intent(this, MovieDetailActivity::class.java)
                .putExtra("movie_id", id)
            startActivity(intent)
        }
        results.layoutManager = resultsLayoutManager
        results.adapter = resultsMoviesAdapter
        results.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var farthestLastViewedItemPosition : Int = 0
            var fetchingMovies : Boolean = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.d(this::class.simpleName, "${resultsLayoutManager.findLastVisibleItemPosition()} -- $farthestLastViewedItemPosition")
                if (resultsLayoutManager.findLastVisibleItemPosition() <= farthestLastViewedItemPosition || fetchingMovies) return
                if (resultsLayoutManager.findLastVisibleItemPosition() >= viewModel.moviesResults.results.size - 2) {
                    Log.d(this@SearchActivity::class.simpleName, "Started fetching next page.")
                    fetchingMovies = true
                    viewModel.nextPage().invokeOnCompletion {
                        fetchingMovies = false
                        resultsMoviesAdapter.notifyItemRangeInserted(viewModel.moviesResults.results.size, viewModel.newMoviesFlow.value.results.size)
                    }
                }
                farthestLastViewedItemPosition = resultsLayoutManager.findLastVisibleItemPosition()
            }
        })

        val onSearchResult = {
            if (viewModel.moviesResults.results.isEmpty()) { // If the initial search provided no results.
                noMoviesFoundText.visibility = View.VISIBLE
            }
            findViewById<View>(R.id.search_results_loading).visibility = View.GONE
            resultsMoviesAdapter.notifyItemRangeInserted(0, viewModel.newMoviesFlow.value.results.size)
        }

        if (intent.action == ACTION_SEARCH_BY_GENRE) {
            val filterButton = findViewById<Button>(R.id.search_filter_open)
            filterButton.visibility = if (intent.action == ACTION_SEARCH_BY_GENRE) View.VISIBLE else View.GONE
            val filterBottomSheet = SearchFilterBottomSheetFragment(viewModel) {
                viewModel.searchGenres(genreId).invokeOnCompletion {
                    resultsMoviesAdapter.notifyDataSetChanged()
                }
            }
            filterButton.setOnClickListener {
                filterBottomSheet.show(supportFragmentManager, SearchFilterBottomSheetFragment.TAG)
            }
        }

        if (savedInstanceState == null) {
            when (intent.action) {
                ACTION_SEARCH_BY_GENRE -> {
                    viewModel.searchGenres(genreId).invokeOnCompletion { onSearchResult() }
                }

                else -> { // ACTION_SEARCH_BY_MOVIE
                    viewModel.searchMovies(searchText).invokeOnCompletion { onSearchResult() }
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
        const val EXTRA_GENRE_ID = "iut.s4.sae.EXTRA_GENREID"
    }
}