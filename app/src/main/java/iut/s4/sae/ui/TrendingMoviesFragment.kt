package iut.s4.sae.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import iut.s4.sae.R

private const val ARG_PARAM1 = "movies"
private const val ARG_PARAM2 = "genres"

/**
 * Displays a carousel containing current trending movies,
 * as well as a grid of movie genres beneath.
 */
class TrendingMoviesFragment : Fragment() {
    private var trendingMovies: Array<Any>? = null
    private var movieGenres: Array<Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //trendingMovies = it.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trending_movies, container, false)
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
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(movies: Array<Any>, genres: Array<Any>) =
            TrendingMoviesFragment().apply {
                arguments = Bundle().apply {
                    //putParcelableArray(ARG_PARAM1, movies)
                    //putParcelableArray(ARG_PARAM2, genres)
                }
            }
    }
}