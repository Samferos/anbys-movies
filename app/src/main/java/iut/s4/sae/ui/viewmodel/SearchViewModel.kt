package iut.s4.sae.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iut.s4.sae.action.ACTION_SEARCH_BY_GENRE
import iut.s4.sae.action.ACTION_SEARCH_BY_MOVIE
import iut.s4.sae.model.MovieSortType
import iut.s4.sae.model.Movies
import iut.s4.sae.model.getSortParam
import iut.s4.sae.network.MovieDao
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * This [ViewModel] allows querying movies
 * by title or by genre dynamically.
 *
 * Ideally, you'd prepare a search by setting global search settings
 * with [setSearchSettings] and then launch [searchMovies] or
 * [searchGenres] and then follow with [nextPage] as
 * the user scrolls through [moviesResults].
 *
 * You can collect the new movies that are added by the
 * new page by subscribing to [newMoviesFlow].
 */
class SearchViewModel : ViewModel() {
    private var searchType: String = ACTION_SEARCH_BY_MOVIE
    private var genreId: Int = 0
    private var query: String = ""
    private var currentPage: Int = 1
    private var language: String = "en"
    private var includeAdult: Boolean = false
    var sortType : MovieSortType = MovieSortType.BY_POPULARITY
    var ascendingSort : Boolean = false

    private val _newMoviesFlow = MutableStateFlow(Movies(mutableListOf()))
    val newMoviesFlow: StateFlow<Movies> = _newMoviesFlow.asStateFlow()
    var moviesResults = Movies(mutableListOf())
        private set

    /**
     * Sets the search global settings.
     * Meant to be only called before starting a new search.
     */
    fun setSearchSettings(language: String, includeAdult: Boolean) {
        this.language = language
        this.includeAdult = includeAdult
    }

    /**
     * Starts a new search for movies.
     * @param query The search input to use.
     * @return A [Job] representing the status of the
     * movies list fetch.
     */
    fun searchMovies(query: String): Job {
        this.query = query
        searchType = ACTION_SEARCH_BY_MOVIE
        currentPage = 1
        return viewModelScope.launch {
            val newMovies = MovieDao.getInstance().searchMovies(
                query, currentPage, language, includeAdult
            )
            moviesResults.results.clear()
            moviesResults.results.addAll(newMovies.results)
            _newMoviesFlow.value = newMovies
        }
    }

    /**
     * Starts a new search for movies by genre.
     * @param genreId The chosen genre ID.
     * @return A [Job] representing the status of the
     * movies list fetch.
     */
    fun searchGenres(genreId: Int? = null): Job {
        if (genreId != null)
            this.genreId = genreId
        searchType = ACTION_SEARCH_BY_GENRE
        currentPage = 1
        return viewModelScope.launch {
            val newMovies = MovieDao.getInstance().discoverByGenre(
                genreId ?: this@SearchViewModel.genreId, currentPage,
                getSortParam(sortType, ascendingSort),
                includeAdult,
                language
            )
            moviesResults.results.clear()
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
     *
     * @return the next page fetching [Job]
     */
    fun nextPage(): Job {
        return viewModelScope.launch {
            when (searchType) {
                ACTION_SEARCH_BY_MOVIE -> {
                    val newMovies = MovieDao.getInstance().searchMovies(
                        query, ++currentPage, language, includeAdult
                    )
                    moviesResults.results.addAll(newMovies.results)
                    _newMoviesFlow.value = newMovies
                }

                ACTION_SEARCH_BY_GENRE -> {
                    val newMovies = MovieDao.getInstance().discoverByGenre(
                        genreId,
                        ++currentPage,
                        getSortParam(sortType, ascendingSort),
                        includeAdult,
                        language
                    )
                    moviesResults.results.addAll(newMovies.results)
                    _newMoviesFlow.value = newMovies
                }
            }
        }
    }
}