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
import iut.s4.sae.model.Movies

class CarouselMovieAdapter(private var movies: Movies, private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<CarouselMovieAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var poster : ImageView = view.findViewById(R.id.trending_movie_entry_image)
        var title : TextView = view.findViewById(R.id.trending_movie_entry_title)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.trending_movie_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = movies.results[position].title
        if (movies.results[position].backdropPath != null) {
            val image = Picasso.get().load("https://image.tmdb.org/t/p/original${movies.results[position].backdropPath}")
                .into(holder.poster)
        }
        holder.itemView.setOnClickListener{
            onItemClick(position)
        }
        (holder.itemView as MaskableFrameLayout).setOnMaskChangedListener {
            maskRect ->
            holder.title.translationX = maskRect.left
        }
    }

    fun updateMovies(newMovies: Movies) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun getItemCount() = movies.results.size
}