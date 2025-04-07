package iut.s4.sae.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.android.material.carousel.CarouselLayoutManager
import iut.s4.sae.R
import iut.s4.sae.model.Genres
import iut.s4.sae.model.Movies

private const val ARG_PARAM1 = "movies"
private const val ARG_PARAM2 = "genres"

/**
 * Displays a carousel containing current trending movies,
 * as well as a grid of movie genres beneath.
 */
class TrendingMoviesFragment : Fragment() {
    private var trendingMovies: Movies? = null
    private var movieGenres: Genres? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trendingMovies = requireArguments().getParcelable(ARG_PARAM1, Movies::class.java)
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
        carousel?.adapter = TrendingMovieAdapter(trendingMovies ?: Movies(listOf()))
        carousel?.layoutManager = CarouselLayoutManager()
    }

    companion object {
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
                    putParcelable(ARG_PARAM1, movies)
                    putParcelable(ARG_PARAM2, genres)
                }
            }
    }
}