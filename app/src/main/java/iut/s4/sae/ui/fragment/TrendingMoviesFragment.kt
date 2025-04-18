package iut.s4.sae.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.chip.Chip
import iut.s4.sae.R
import iut.s4.sae.SettingsManager
import iut.s4.sae.action.ACTION_SEARCH_BY_GENRE
import iut.s4.sae.model.Genres
import iut.s4.sae.model.Movies
import iut.s4.sae.ui.MovieDetailActivity
import iut.s4.sae.ui.SearchActivity
import iut.s4.sae.ui.adapter.CarouselMovieAdapter
import iut.s4.sae.ui.adapter.GenreAdapter
import iut.s4.sae.ui.viewmodel.TrendingMoviesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Displays a carousel containing current trending movies,
 * as well as a grid of movie genres beneath.
 */
class TrendingMoviesFragment(
    private val viewmodel : TrendingMoviesViewModel
) : Fragment() {
    private var trendingMovies: Movies? = null
    private var movieGenres: Genres? = null

    val allowAdult : Boolean
        get() = SettingsManager.isAdultContentAllowed(requireContext())
    val language : String
        get() = SettingsManager.getPreferredLanguage(requireContext())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_trending_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null && !viewmodel.hasSynced) {
            viewmodel.run {
                syncTrendingMovieData(language)
                syncGenresData(language)
            }
        }

        val carousel = view.findViewById<RecyclerView>(R.id.trending_movies_fragment_carousel)

        val carouselMovieAdapter = CarouselMovieAdapter(viewmodel.trendingMovies.value) {
            tappedMovie ->
            val intent = Intent(requireContext(), MovieDetailActivity::class.java).apply {
                putExtra("movie_id", tappedMovie.id)
            }
            startActivity(intent)
        }

        carousel.adapter = carouselMovieAdapter
        carousel.layoutManager = CarouselLayoutManager()

        val trendingFilterDaily = view.findViewById<Chip>(R.id.trending_movies_fragment_daily_chip)
        val trendingFilterWeekly = view.findViewById<Chip>(R.id.trending_movies_fragment_weekly_chip)

        trendingFilterDaily.isChecked = viewmodel.timewindow == "day"
        trendingFilterWeekly.isChecked = viewmodel.timewindow == "week"

        trendingFilterDaily.setOnClickListener {
            trendingFilterDaily.isChecked = true
            trendingFilterWeekly.isChecked = false
            viewmodel.timewindow = "day"
            viewmodel.syncTrendingMovieData(language)
        }

        trendingFilterWeekly.setOnClickListener {
            trendingFilterWeekly.isChecked = true
            trendingFilterDaily.isChecked = false
            viewmodel.timewindow = "week"
            viewmodel.syncTrendingMovieData(language)
        }

        val genreList = view.findViewById<RecyclerView>(R.id.trending_movies_fragment_genres)
        val genreListAdapter = GenreAdapter(viewmodel.genres.value) {
            genre ->
            searchGenre(genre.id, genre.name)
        }

        val genreListLayoutManager = GridLayoutManager(this.context, 2)
        genreList.adapter = genreListAdapter
        genreList.layoutManager = genreListLayoutManager

        val carouselLoader = view.findViewById<View>(R.id.trending_movies_fragment_carousel_loading)
        val genresLoader = view.findViewById<View>(R.id.trending_movies_fragment_genres_loading)
        val errorText = view.findViewById<View>(R.id.trending_movies_fragment_error)

        // Loading display
        val loadingWatchdog = lifecycleScope.launch {
            carousel.visibility = View.GONE
            genreList.visibility = View.GONE
            carouselLoader.visibility = View.VISIBLE
            genresLoader.visibility = View.VISIBLE
            delay(3000)
            if (viewmodel.hasSynced) {
                carousel.visibility = View.VISIBLE
                genreList.visibility = View.VISIBLE
            }
            else {
                errorText.visibility = View.VISIBLE
            }
            carouselLoader.visibility = View.GONE
            genresLoader.visibility = View.GONE
        }
        loadingWatchdog.invokeOnCompletion {
            carousel.visibility = View.VISIBLE
            genreList.visibility = View.VISIBLE
            carouselLoader.visibility = View.GONE
            genresLoader.visibility = View.GONE
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.trendingMovies.collect {
                carouselMovieAdapter.updateMovies(it)
                if (loadingWatchdog.isActive && it.results.isNotEmpty()) {
                    loadingWatchdog.cancel()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.genres.collect {
                genreListAdapter.updateGenres(it)
                if (loadingWatchdog.isActive && it.genres.isNotEmpty()) {
                    loadingWatchdog.cancel()
                }
            }
        }
    }

    private fun searchGenre(genreId: Int, genreName: String) {
        val intent = Intent(this.context, SearchActivity::class.java)
        intent
            .putExtra(SearchActivity.EXTRA_GENRE_ID, genreId)
            .putExtra(SearchActivity.EXTRA_SEARCH, genreName)
            .action = ACTION_SEARCH_BY_GENRE
        startActivity(intent)
    }
}