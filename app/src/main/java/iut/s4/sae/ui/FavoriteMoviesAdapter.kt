package iut.s4.sae.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import iut.s4.sae.R
import iut.s4.sae.model.Movies

class FavoriteMoviesAdapter(private var movies : Movies) : RecyclerView.Adapter<FavoriteMoviesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title : TextView = view.findViewById(R.id.tv_movie_title)
        var genre : TextView = view.findViewById(R.id.tv_movie_genre)
        var releaseDate : TextView = view.findViewById(R.id.tv_movie_release_date)
        var poster : ImageView = view.findViewById(R.id.iv_movie_poster)
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
        holder: FavoriteMoviesAdapter.ViewHolder,
        position: Int
    ) {
        holder.title.text = movies.results[position].title
        holder.genre.text = movies.results[position].genres.joinToString(separator = " · ") { it.name }
        holder.releaseDate.text = movies.results[position].releaseDate
        if (movies.results[position].posterPath == null) {
            holder.poster.setImageResource(R.drawable.media)
        } else {
            val image = Picasso.get().load("https://image.tmdb.org/t/p/original${movies.results[position].posterPath}")
                .into(holder.poster)
        }
    }

    fun updateMovies(newMovies: Movies) {
        movies = newMovies
        notifyDataSetChanged()
    }

    fun removeMovie(position: Int) {
        val newList = movies.results.toMutableList()
        newList.removeAt(position)
        updateMovies(Movies(newList.toList()))
        notifyItemRemoved(position)
    }


    override fun getItemCount() = movies.results.size
}