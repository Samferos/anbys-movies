package iut.s4.sae.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iut.s4.sae.model.Movie
import iut.s4.sae.model.Movies
import iut.s4.sae.network.MovieDao
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing and providing data related to movie details and similar movies.
 *
 * It interacts with [MovieDao] to fetch movie details and similar movies from a network source,
 * and exposes them through observable state flows for use in the UI layer.
 */
class MovieDetailViewModel : ViewModel() {

    /**
     * Holds the currently selected movie's details.
     *
     * This value is updated when [fetchMovieDetails] is called.
     */
    var movie: Movie = Movie(false, id = 0, title = "")
        private set

    /**
     * Backing property for [similarMovies], holding a list of similar movies.
     */
    private val _similarMovies = MutableStateFlow(Movies(mutableListOf()))

    /**
     * A [StateFlow] that represents the list of movies similar to the currently selected one.
     *
     * Observers can collect from this flow to get updates when similar movies are fetched.
     */
    val similarMovies: StateFlow<Movies> = _similarMovies.asStateFlow()

    /**
     * Fetches detailed information about a specific movie by its [movieId] and [language].
     *
     * The result is assigned to the [movie] property.
     *
     * @param movieId The ID of the movie to fetch details for.
     * @param language The language in which to retrieve movie details.
     * @return A [Job] representing the coroutine launched for the fetch operation.
     */
    fun fetchMovieDetails(movieId: Int, language: String): Job {
        return viewModelScope.launch {
            movie = MovieDao.getInstance().searchMovieDetails(movieId.toString(), language)
        }
    }

    /**
     * Fetches a list of movies similar to the movie with the given [id] and [language].
     *
     * Updates the [similarMovies] flow with the result.
     *
     * @param id The ID of the movie to find similar movies for.
     * @param language The language in which to retrieve similar movie data.
     */
    fun fetchSimilarMovies(id: Int, language: String) {
        viewModelScope.launch {
            _similarMovies.value = MovieDao.getInstance().fetchSimilarMovies(id, language = language)
        }
    }
}
