package iut.s4.sae.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso
import iut.s4.sae.R
import iut.s4.sae.model.Movies

/**
 * Adapter class for displaying a list of movies in a RecyclerView.
 *
 * @param movies Initial list of movies to display. Defaults to an empty list.
 * @param onItemClick Lambda function triggered when a movie item is clicked, passing the position of the item.
 */
class MovieEntriesAdapter(
    private var movies: Movies = Movies(mutableListOf()),
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<MovieEntriesAdapter.ViewHolder>() {

    /**
     * ViewHolder class representing a single movie item view in the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.tv_movie_title)
        var genre: TextView = view.findViewById(R.id.tv_movie_genre)
        var releaseDate: TextView = view.findViewById(R.id.tv_movie_release_date)
        var poster: ImageView = view.findViewById(R.id.iv_movie_poster)
        var adultTag: Chip = view.findViewById(R.id.tv_movie_adult_tag)
        var description: TextView = view.findViewById(R.id.tv_movie_description)
    }

    /**
     * Creates a new ViewHolder by inflating the movie_entry layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_entry, parent, false)
        return ViewHolder(view)
    }

    /**
     * Binds data from a movie item to the ViewHolder's views.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies.results[position]

        holder.title.text = movie.title
        holder.genre.text = movie.genres.joinToString(separator = " · ") { it.name }
        holder.releaseDate.text = movie.releaseDate

        // Show adult content tag only if applicable
        holder.adultTag.visibility = if (movie.adult) View.VISIBLE else View.GONE

        // Trim the overview if it's too long
        holder.description.text = movie.overview?.let {
            if (it.length > 200) it.slice(0..200) + "..." else it
        }

        // Load poster image using Picasso
        if (movie.posterPath != null) {
            Picasso.get()
                .load("https://image.tmdb.org/t/p/original${movie.posterPath}")
                .into(holder.poster)
        } else {
            holder.poster.setImageResource(R.drawable.anby_slam_tv_bw)
        }

        // Set up item click listener
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    /**
     * Returns the number of movies currently in the list.
     */
    override fun getItemCount() = movies.results.size

    /**
     * Adds a list of movies to the existing data set and notifies the adapter.
     *
     * @param moviesToAdd Movies to be added.
     */
    fun addMovies(moviesToAdd: Movies) {
        val originalSize = movies.results.size
        movies.results.addAll(moviesToAdd.results)
        notifyItemRangeInserted(originalSize, moviesToAdd.results.size)
    }

    /**
     * Removes a movie at the specified position and notifies the adapter.
     *
     * @param position Index of the movie to remove.
     */
    fun removeMovie(position: Int) {
        movies.results.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * Returns the current list of movies.
     */
    fun getMovies(): Movies {
        return movies
    }
}
