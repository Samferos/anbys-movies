package iut.s4.sae.ui.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.chip.Chip
import iut.s4.sae.R
import iut.s4.sae.SettingsManager
import iut.s4.sae.action.ACTION_SEARCH_BY_GENRE
import iut.s4.sae.model.Genres
import iut.s4.sae.model.Movies
import iut.s4.sae.network.MovieDao
import iut.s4.sae.ui.MovieDetailActivity
import iut.s4.sae.ui.SearchActivity
import iut.s4.sae.ui.adapter.CarouselMovieAdapter
import iut.s4.sae.ui.adapter.GenreAdapter
import kotlinx.coroutines.runBlocking

/**
 * Displays a carousel containing current trending movies,
 * as well as a grid of movie genres beneath.
 */
class TrendingMoviesFragment : Fragment() {
    private var trendingMovies: Movies? = null
    private var movieGenres: Genres? = null

    val allowAdult : Boolean
        get() = SettingsManager.isAdultContentAllowed(requireContext())
    val language : String
        get() = SettingsManager.getPreferredLanguage(requireContext())

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trendingMovies = requireArguments().getParcelable(EXTRA_MOVIES, Movies::class.java)
        movieGenres = requireArguments().getParcelable(EXTRA_GENRES, Genres::class.java)
        Log.d(this::class.simpleName, "Retrieved movies $trendingMovies")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trending_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val carousel = view.findViewById<RecyclerView>(R.id.trending_movies_fragment_carousel)

        val carouselMovieAdapter = CarouselMovieAdapter(viewmodel.trendingMovies.value) {
            tappedMovie ->
            val intent = Intent(requireContext(), MovieDetailActivity::class.java).apply {
                putExtra("movie_id", tappedMovie.id)
            }
            startActivity(intent)
        }
        carousel?.adapter = carouselMovieAdapter
        carousel?.layoutManager = CarouselLayoutManager()

        val trendingFilterDaily = view.findViewById<Chip>(R.id.trending_movies_fragment_daily_chip)
        val trendingFilterWeekly = view.findViewById<Chip>(R.id.trending_movies_fragment_weekly_chip)

        trendingFilterDaily.isChecked = true

        trendingFilterDaily.setOnClickListener {
            trendingFilterDaily.isChecked = true
            trendingFilterWeekly.isChecked = false
            runBlocking {
                trendingMovies = MovieDao.getInstance().fetchTrendingMovies("day", language)
            }
            carouselMovieAdapter.updateMovies(trendingMovies ?: Movies(mutableListOf()))
        }

        trendingFilterWeekly.setOnClickListener {
            trendingFilterWeekly.isChecked = true
            trendingFilterDaily.isChecked = false
            runBlocking {
                trendingMovies = MovieDao.getInstance().fetchTrendingMovies("week", language)
            }
            carouselMovieAdapter.updateMovies(trendingMovies ?: Movies(mutableListOf()))
        }

        val genreList = view.findViewById<RecyclerView>(R.id.trending_movies_fragment_genres)
        val genreListAdapter = GenreAdapter(viewmodel.genres.value) {
            genre ->
            searchGenre(genre.id, genre.name)
        }

        val genreListLayoutManager = GridLayoutManager(this.context, 2)
        genreList.adapter = genreListAdapter
        genreList.layoutManager = genreListLayoutManager
    }

    private fun searchGenre(genreId: Int, genreName: String) {
        val intent = Intent(this.context, SearchActivity::class.java)
        intent
            .putExtra(SearchActivity.EXTRA_GENRE_ID, genreId)
            .putExtra(SearchActivity.EXTRA_SEARCH, genreName)
            .action = ACTION_SEARCH_BY_GENRE
        startActivity(intent)
    }

    companion object {

        private const val EXTRA_MOVIES = "iut.s4.sae.EXTRA_MOVIES"
        private const val EXTRA_GENRES = "iut.s4.sae.EXTRA_GENRE"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param movies The list of trending movies to be displayed
         * in the carousel.
         * @param genres The list of genres to be displayed in a
         * grid of cards.
         * @return A new instance of fragment TrendingMoviesFragment.
         */
        @JvmStatic
        fun newInstance(movies: Movies, genres: Genres) =
            TrendingMoviesFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_MOVIES, movies)
                    putParcelable(EXTRA_GENRES, genres)
                }
            }
    }
}