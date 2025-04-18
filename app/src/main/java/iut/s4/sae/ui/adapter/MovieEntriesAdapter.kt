package iut.s4.sae.ui.adapter

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso
import iut.s4.sae.R
import iut.s4.sae.model.Movies
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MovieEntriesAdapter(private var movies : Movies = Movies(mutableListOf()), private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<MovieEntriesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title : TextView = view.findViewById(R.id.tv_movie_title)
        var genre : TextView = view.findViewById(R.id.tv_movie_genre)
        var releaseDate : TextView = view.findViewById(R.id.tv_movie_release_date)
        var poster : ImageView = view.findViewById(R.id.iv_movie_poster)
        var adultTag : Chip = view.findViewById(R.id.tv_movie_adult_tag)
        var description : TextView = view.findViewById(R.id.tv_movie_description)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.title.text = movies.results[position].title
        holder.genre.text = movies.results[position].genres.joinToString(separator = " · ") { it.name }
        holder.releaseDate.text = movies.results[position].releaseDate
        holder.adultTag.visibility = if (movies.results[position].adult) View.VISIBLE else View.GONE
        holder.description.text = movies.results[position].overview.let {
            if ((it?.length ?: 0) > 200)
                it?.slice(0..200) + "..."
            else
                it
        }
        if (movies.results[position].posterPath != null) {
            val image = Picasso.get().load("https://image.tmdb.org/t/p/original${movies.results[position].posterPath}")
                .into(holder.poster)
        } else {
            holder.poster.setImageResource(R.drawable.anby_slam_tv_bw)
        }
        holder.itemView.setOnClickListener{
            onItemClick(position)
        }
    }

    fun addMovies(moviesToAdd : Movies) {
        val originalSize = movies.results.size
        movies.results.addAll(moviesToAdd.results)
        notifyItemRangeInserted(originalSize, moviesToAdd.results.size)
    }

    fun removeMovie(position: Int) {
        movies.results.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getMovies() : Movies {
        return movies
    }

    override fun getItemCount() = movies.results.size
}