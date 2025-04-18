package iut.s4.sae.ui.fragment

import android.content.Context.MODE_PRIVATE
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
import iut.s4.sae.ui.MovieDetailActivity
import iut.s4.sae.ui.adapter.MovieEntriesAdapter
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.graphics.withClip
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FavoriteMoviesFragment : Fragment() {
    private var favoriteMovies : Movies? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteMovies = requireArguments().getParcelable(EXTRA_MOVIES, Movies::class.java)
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
        val movieEntriesAdapter = MovieEntriesAdapter(favoriteMovies ?: Movies(mutableListOf())) { position ->
            val clickedMovie = favoriteMovies?.results?.get(position)
            val movieId = clickedMovie?.id ?: return@MovieEntriesAdapter
            val intent = Intent(requireContext(), MovieDetailActivity::class.java).apply {
                putExtra("movie_id", movieId)
            }
            startActivity(intent)
        }

        recyclerViewFavoriteMovies.adapter = movieEntriesAdapter
        recyclerViewFavoriteMovies.layoutManager = LinearLayoutManager(context)

        val llNoMovie : LinearLayout = view.findViewById(R.id.ll_no_movie)
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
                movieEntriesAdapter.removeMovie(position)

                context?.getSharedPreferences("favorites", MODE_PRIVATE)?.edit() {
                    val updatedJson = Json.encodeToString(movieEntriesAdapter.getMovies())
                    putString("favorite_movies", updatedJson)
                }


            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX < 0) {
                    val itemView = viewHolder.itemView
                    val paint = Paint()
                    paint.color = Color.RED

                    // Draw the red background within clipped bounds
                    c.withClip(
                        itemView.right + dX - 50,
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat()
                    ) {
                        drawRect(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat(),
                            paint
                        )
                    }

                    // Load and draw the icon
                    val icon = ContextCompat.getDrawable(recyclerView.context, R.drawable.round_close_24)
                    icon?.let {
                        val iconMargin = (itemView.height - it.intrinsicHeight) / 2
                        val iconTop = itemView.top + iconMargin
                        val iconBottom = iconTop + it.intrinsicHeight
                        val iconRight = itemView.right - iconMargin + (iconMargin/2)
                        val iconLeft = iconRight - it.intrinsicWidth

                        it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        it.draw(c)
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewFavoriteMovies)
    }

    companion object {

        private const val EXTRA_MOVIES = "iut.s4.sae.EXTRA_MOVIES"

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
                    putParcelable(EXTRA_MOVIES, movies)
                }
            }
    }
}