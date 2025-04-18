package iut.s4.sae.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iut.s4.sae.R
import iut.s4.sae.model.Genre
import iut.s4.sae.model.Genres

class GenreAdapter(
    private var genres: Genres,
    private val onGenreClick: (Genre) -> Unit
) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.genre_entry_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.genre_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = genres.genres[position].name
        holder.itemView.setOnClickListener {
            onGenreClick(genres.genres[position])
        }
    }

    override fun getItemCount(): Int = genres.genres.size

    fun updateGenres(newGenres: Genres) {
        genres = newGenres
        val difference = genres.genres.size - newGenres.genres.size
        if (difference < 0) {
            notifyItemRangeRemoved(0, genres.genres.size)
        }
        else if (difference > 0) {
            notifyItemRangeInserted(0, genres.genres.size)
        }
        notifyItemRangeChanged(0, genres.genres.size)
    }
}
