package iut.s4.sae.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iut.s4.sae.model.Movies
import iut.s4.sae.network.MovieDao
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * This [ViewModel] allows a searchview to start a movie query
 * by title or by genre.
 * Ideally, you initialize a search with [searchMovies] or
 * [searchGenres] and then follow-up with [nextPage] as
 * the user scrolls through [moviesResults].
 *
 * You can collect the new movies that are added by the
 * new page by subscribing to [newMoviesFlow].
 */
class SearchViewModel : ViewModel() {
    private var searchType: SearchType = SearchType.MOVIE_SEARCH
    private var genreId: Int = 0
    private var query: String = ""
    private var currentPage: Int = 1
    var language: String = "en"
    var includeAdult: Boolean = false

    private val _newMoviesFlow = MutableStateFlow(Movies(mutableListOf()))
    val newMoviesFlow: StateFlow<Movies> = _newMoviesFlow
    val moviesResults = _newMoviesFlow.value

    /**
     * Starts a search for movies.
     * @param query The search input to use.
     * @return A job representing the status of the
     * movies list fetch.
     */
    fun searchMovies(query: String): Job {
        this.query = query
        searchType = SearchType.MOVIE_SEARCH
        return viewModelScope.launch {
            val newMovies = MovieDao.getInstance().searchMovies(
                query, currentPage, language, includeAdult
            )
            moviesResults.results.addAll(newMovies.results)
            _newMoviesFlow.value = newMovies
        }
    }

    /**
     * Starts a search by genres.
     * @param genreId The chosen genre ID.
     * @return A job representing the status of the
     * movies list fetch.
     */
    fun searchGenres(genreId: Int): Job {
        this.genreId = genreId
        searchType = SearchType.GENRE_SEARCH
        return viewModelScope.launch {
            val newMovies = MovieDao.getInstance().discoverByGenre(
                genreId, currentPage, language = language, includeAdult = includeAdult
            )
            moviesResults.results.addAll(newMovies.results)
            _newMoviesFlow.value = newMovies
        }
    }

    /**
     * Depending on the previous search, fetches the next page
     * and emits the new movies to [newMoviesFlow].
     * This is intended to be called by a [androidx.recyclerview.widget.RecyclerView]
     * in a `OnScroll` listener, to load the new movies when reaching
     * the end of the page.
     */
    fun nextPage() {
        viewModelScope.launch {
            when (searchType) {
                SearchType.MOVIE_SEARCH -> {
                    val newMovies = MovieDao.getInstance().searchMovies(
                        query, ++currentPage, language, includeAdult
                    )
                    moviesResults.results.addAll(newMovies.results)
                    _newMoviesFlow.value = newMovies
                }

                SearchType.GENRE_SEARCH -> {
                    val newMovies = MovieDao.getInstance().discoverByGenre(
                        genreId ?: 0,
                        ++currentPage,
                        language = language,
                        includeAdult = includeAdult
                    )
                    moviesResults.results.addAll(newMovies.results)
                    _newMoviesFlow.value = newMovies
                }
            }
        }
    }
}