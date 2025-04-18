package iut.s4.sae.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iut.s4.sae.model.Genres
import iut.s4.sae.model.Movies
import iut.s4.sae.network.MovieDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Holds currently trending movies and
 * a list of existing movie genres.
 */
class TrendingMoviesViewModel : ViewModel() {

    /**
     * Sets the time window that will dictate the
     * [trendingMovies] results.
     *
     * Can be `day` or `week`.
     */
    var timewindow = "day"

    private val _trendingMovies = MutableStateFlow(Movies(mutableListOf()))
    val trendingMovies : StateFlow<Movies> = _trendingMovies.asStateFlow()
    private val _genres = MutableStateFlow(Genres(mutableListOf()))
    val genres : StateFlow<Genres> = _genres.asStateFlow()

    var hasSynced = false
    private set

    /**
     * Syncs trending movies.
     */
    fun syncTrendingMovieData(language : String) {
        viewModelScope.launch {
            _trendingMovies.value = MovieDao.getInstance().fetchTrendingMovies(timewindow, language)
        }
        hasSynced = true
    }

    /**
     * Syncs available movie genres.
     * Supposedly, it should not change much, so
     * expect to only call this function once.
     */
    fun syncGenresData(language : String) {
        viewModelScope.launch {
            _genres.value = MovieDao.getInstance().fetchGenres(language)
        }
        hasSynced = true
    }
}