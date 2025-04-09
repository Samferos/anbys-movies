package iut.s4.sae.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iut.s4.sae.R
import iut.s4.sae.model.Movies

private const val ARG_PARAM1 = "movies"

class FavoriteMoviesFragment : Fragment() {
    private var favoriteMovies : Movies? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteMovies = requireArguments().getParcelable(ARG_PARAM1, Movies::class.java)
        Log.d(this::class.simpleName, "Retrieved movies $favoriteMovies")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewFavoriteMovies = view.findViewById<RecyclerView>(R.id.recycler_view_favorite_movie)
        val favoriteMoviesAdapter = FavoriteMoviesAdapter(favoriteMovies ?: Movies(listOf())) {position ->
            val clickedMovie = favoriteMovies?.results?.get(position)
            val movieId = clickedMovie?.id ?: return@FavoriteMoviesAdapter
            val intent = Intent(requireContext(), MovieDetailActivity::class.java).apply {
                putExtra("movie_id", movieId)
            }
            startActivity(intent)
        }

        recyclerViewFavoriteMovies.adapter = favoriteMoviesAdapter
        recyclerViewFavoriteMovies.layoutManager = LinearLayoutManager(context)

        val llNoMovie : LinearLayout = view.findViewById<LinearLayout>(R.id.ll_no_movie)
        if (favoriteMovies!!.results.isEmpty()) {
            llNoMovie.visibility = View.VISIBLE
        } else {
            llNoMovie.visibility = View.GONE
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                favoriteMoviesAdapter.removeMovie(requireContext(),position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewFavoriteMovies)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param movies The list of favorite movies to be displayed.
         *
         * @return A new instance of fragment FavoriteMoviesFragment.
         */
        @JvmStatic
        fun newInstance(movies: Movies) =
            FavoriteMoviesFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, movies)
                }
            }
    }
}