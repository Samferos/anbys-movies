package iut.s4.sae.ui

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.carousel.CarouselLayoutManager
import com.squareup.picasso.Picasso
import iut.s4.sae.R
import iut.s4.sae.model.Movie
import iut.s4.sae.model.Movies
import iut.s4.sae.network.MovieDao
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.math.roundToLong
import androidx.core.content.edit
import androidx.core.view.updateLayoutParams
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.serialization.encodeToString


class MovieDetailActivity : AppCompatActivity() {
    private lateinit var backdrop : ImageView
    private lateinit var poster : ImageView
    private lateinit var titleTextView : TextView
    private lateinit var yearTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var genreTextView: TextView
    private lateinit var runtimeTextView: TextView
    private lateinit var overviewTextView: TextView
    private lateinit var carousel : RecyclerView
    private lateinit var floatingButtonAddFavorite : FloatingActionButton
    private var isFavorite : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        floatingButtonAddFavorite = findViewById(R.id.floating_action_button_add_favorite)
        ViewCompat.setOnApplyWindowInsetsListener(floatingButtonAddFavorite) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = systemBars.bottom + 8.dp
            }

            insets
        }
        val topBar = findViewById<MaterialToolbar>(R.id.movie_detail_topbar)
        val movieId = intent.getIntExtra("movie_id", -1)
        val movie : Movie?

        backdrop = findViewById(R.id.movie_detail_backdrop)
        poster = findViewById(R.id.movie_detail_poster)
        titleTextView = findViewById(R.id.movie_detail_title)
        yearTextView = findViewById(R.id.movie_detail_year)
        scoreTextView = findViewById(R.id.movie_detail_score)
        dateTextView = findViewById(R.id.movie_detail_date)
        genreTextView = findViewById(R.id.movie_detail_genre)
        runtimeTextView = findViewById(R.id.movie_detail_runtime)
        overviewTextView = findViewById(R.id.movie_detail_overview)
        carousel = findViewById(R.id.movie_detail_carousel)

        runBlocking {
            movie = MovieDao.getInstance().searchMovieDetails(movieId.toString())
        }

        Picasso.get().load("https://image.tmdb.org/t/p/original${movie?.backdropPath}")
            .into(backdrop)
        Picasso.get().load("https://image.tmdb.org/t/p/original${movie?.posterPath}")
            .into(poster)

        topBar.setNavigationOnClickListener {
            finish()
        }
        titleTextView.text = movie?.title
        yearTextView.text = ("(${movie?.releaseDate?.split("-")?.get(0) ?: ""})")
        dateTextView.text = movie?.releaseDate
        scoreTextView.text = ("${movie?.voteAverage?.roundToLong()}/10")
        genreTextView.text = movie?.genres?.joinToString(" - ") { it.name }
        runtimeTextView.text = formatRuntime(movie?.runtime)
        overviewTextView.text = movie?.overview

        val similarMovie : Movies

        runBlocking {
            similarMovie = MovieDao.getInstance().fetchSimilarMovies(movieId)
        }

        val similarMovieAdapter = SimilarMovieAdapter(similarMovie ?: Movies(listOf())) { position ->
            val clickedMovie = similarMovie.results[position]
            val _movieId = clickedMovie.id ?: return@SimilarMovieAdapter
            val intent = Intent(this, MovieDetailActivity::class.java).apply {
                putExtra("movie_id", _movieId)
            }
            startActivity(intent)
        }

        carousel.adapter = similarMovieAdapter
        carousel.layoutManager = CarouselLayoutManager()

        if (movie != null) {
            isFavorite = isMovieFavorite(this@MovieDetailActivity, movie)
            updateFloatingActionButton()
        }
        floatingButtonAddFavorite.setOnClickListener {
            if (movie != null) {
                toggleFavorite(this@MovieDetailActivity, movie)
                updateFloatingActionButton()
            }
        }


    }

    private fun formatRuntime(runtimeInMinutes: Int?): String {
        return if (runtimeInMinutes != null) {
            val hours = runtimeInMinutes / 60
            val minutes = runtimeInMinutes % 60
            "${hours}h${minutes}"
        } else {
            "N/A"
        }
    }

    private fun toggleFavorite(context: Context, movie: Movie) {
        val sharedPreferences = context.getSharedPreferences("favorites", MODE_PRIVATE)
        val jsonMovies = sharedPreferences.getString("favorite_movies", null)
        val movies = if (jsonMovies != null) {
            Json.decodeFromString<Movies>(jsonMovies)
        } else {
            Movies(emptyList())
        }

        val updatedMovies = if (isFavorite) {
            movies.results.filterNot { it.id == movie.id }
        } else {
            movies.results + movie
        }
        isFavorite = !isFavorite
        sharedPreferences.edit() {
            val updatedJson = Json.encodeToString(Movies(updatedMovies))
            putString("favorite_movies", updatedJson)
        }
    }

    private fun isMovieFavorite(context: Context, movie: Movie): Boolean {
        val sharedPreferences = context.getSharedPreferences("favorites", MODE_PRIVATE)
        val json = sharedPreferences.getString("favorite_movies", null) ?: return false
        val movies = Json.decodeFromString<Movies>(json)
        return movies.results.any { it.id == movie.id }
    }

    private fun updateFloatingActionButton() {
        if (isFavorite) {
            floatingButtonAddFavorite.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            floatingButtonAddFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()


}