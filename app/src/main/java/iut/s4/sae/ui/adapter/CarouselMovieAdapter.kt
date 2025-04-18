package iut.s4.sae.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.MaskableFrameLayout
import com.squareup.picasso.Picasso
import iut.s4.sae.R
import iut.s4.sae.model.Movie
import iut.s4.sae.model.Movies

/**
 * Adapter class for displaying a horizontal carousel of movies using a RecyclerView.
 *
 * @param movies Initial list of movies to display.
 * @param onItemClick Callback triggered when a movie is clicked, passing the selected Movie.
 */
class CarouselMovieAdapter(
    private var movies: Movies,
    private val onItemClick: (Movie) -> Unit
) : RecyclerView.Adapter<CarouselMovieAdapter.ViewHolder>() {

    /**
     * ViewHolder class representing a single carousel movie item view.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView = view.findViewById(R.id.trending_movie_entry_image)
        val title: TextView = view.findViewById(R.id.trending_movie_entry_title)
    }

    /**
     * Inflates the layout for a carousel item and returns a ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.trending_movie_entry, parent, false)
        return ViewHolder(view)
    }

    /**
     * Binds movie data to the ViewHolder at the given position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies.results[position]
        holder.title.text = movie.title

        // Load movie backdrop image using Picasso
        movie.backdropPath?.let {
            Picasso.get()
                .load("https://image.tmdb.org/t/p/original$it")
                .into(holder.poster)
        }

        // Set click listener for the item
        holder.itemView.setOnClickListener {
            onItemClick(movie)
        }

        // Sync title movement with carousel mask scroll
        (holder.itemView as MaskableFrameLayout).setOnMaskChangedListener { maskRect ->
            holder.title.translationX = maskRect.left
        }
    }

    /**
     * Returns the number of movies in the list.
     */
    override fun getItemCount(): Int = movies.results.size

    /**
     * Updates the current list of movies and refreshes the RecyclerView.
     *
     * @param newMovies New Movies object to replace the current data.
     */
    fun updateMovies(newMovies: Movies) {
        val oldSize = movies.results.size
        val newSize = newMovies.results.size
        movies = newMovies

        // Notify changes based on size difference
        when {
            newSize < oldSize -> {
                notifyItemRangeRemoved(newSize, oldSize - newSize)
            }
            newSize > oldSize -> {
                notifyItemRangeInserted(oldSize, newSize - oldSize)
            }
        }

        // Notify that the updated items may have changed
        notifyItemRangeChanged(0, movies.results.size)
    }
}
