package iut.s4.sae.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iut.s4.sae.R
import iut.s4.sae.model.Genre
import iut.s4.sae.model.Genres

/**
 * Adapter class for displaying a list of genres in a RecyclerView.
 *
 * @param genres Initial list of genres to display.
 * @param onGenreClick Lambda function triggered when a genre item is clicked, passing the selected Genre.
 */
class GenreAdapter(
    private var genres: Genres,
    private val onGenreClick: (Genre) -> Unit
) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    /**
     * ViewHolder class representing a single genre item view in the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.genre_entry_title)
    }

    /**
     * Creates and inflates the ViewHolder for a genre item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.genre_entry, parent, false)
        return ViewHolder(view)
    }

    /**
     * Binds data from a genre item to the ViewHolder.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = genres.genres[position]
        holder.title.text = genre.name

        // Handle click events on the item
        holder.itemView.setOnClickListener {
            onGenreClick(genre)
        }
    }

    /**
     * Returns the number of genre items.
     */
    override fun getItemCount(): Int = genres.genres.size

    /**
     * Updates the list of genres and notifies the adapter of the data change.
     *
     * @param newGenres The new list of genres to replace the current one.
     */
    fun updateGenres(newGenres: Genres) {
        val oldSize = genres.genres.size
        val newSize = newGenres.genres.size
        genres = newGenres

        // Notify changes efficiently based on the size difference
        when {
            newSize < oldSize -> {
                notifyItemRangeRemoved(newSize, oldSize - newSize)
            }
            newSize > oldSize -> {
                notifyItemRangeInserted(oldSize, newSize - oldSize)
            }
        }

        // Notify that the updated items may have changed
        notifyItemRangeChanged(0, genres.genres.size)
    }
}

