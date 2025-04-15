package iut.s4.sae.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iut.s4.sae.R
import iut.s4.sae.model.Genres

class GenreAdapter(
    private val genres: Genres,
    private val onGenreClick: (Int) -> Unit
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
            onGenreClick(genres.genres[position].id)
        }
    }

    override fun getItemCount(): Int = genres.genres.size
}
