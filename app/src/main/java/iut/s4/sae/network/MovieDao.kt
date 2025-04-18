package iut.s4.sae.network

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import iut.s4.sae.model.Genres
import iut.s4.sae.model.Movie
import iut.s4.sae.model.Movies
import iut.s4.sae.BuildConfig

/**
 * Data Access Object for interacting with The Movie Database (TMDb) API.
 * This class handles fetching genres, movies, and detailed movie information using Ktor HTTP client.
 *
 * Implemented as a thread-safe singleton.
 */
class MovieDao private constructor() {

    companion object {
        @Volatile
        private var instance: MovieDao? = null

        /**
         * Returns the singleton instance of MovieDao.
         */
        fun getInstance(): MovieDao {
            return instance ?: synchronized(this) {
                instance ?: MovieDao().also { instance = it }
            }
        }
    }

    // Base URL for TMDb API
    private val BASE_URL = "https://api.themoviedb.org/3"
    private val API_KEY = BuildConfig.API_KEY

    /**
     * Fetches a list of movie genres from the TMDb API.
     *
     * @param language Language code (e.g., "en-US") for the response.
     * @return [Genres] object containing a list of genre entries. Returns an empty list on failure.
     */
    suspend fun fetchGenres(language: String): Genres {
        return KtorClient.client.get("$BASE_URL/genre/movie/list") {
            parameter("api_key", API_KEY)
            parameter("language", language)
        }.body()
    }

    /**
     * Fetches trending movies (daily or weekly) from the TMDb API.
     *
     * @param timeWindow Either "day" or "week" for the trending period.
     * @param language Language code for the response.
     * @return [Movies] object with the list of trending movies.
     */
    suspend fun fetchTrendingMovies(timeWindow: String = "day", language: String): Movies {
        return KtorClient.client.get("$BASE_URL/trending/movie/$timeWindow") {
            parameter("api_key", API_KEY)
            parameter("language", language)
        }.body()
    }

    /**
     * Fetches detailed information about a specific movie.
     *
     * @param movieId The ID of the movie.
     * @param language Language code for the response.
     * @return A [Movie] object with full details.
     */
    suspend fun searchMovieDetails(movieId: String, language: String): Movie {
        return KtorClient.client.get("$BASE_URL/movie/$movieId") {
            parameter("api_key", API_KEY)
            parameter("language", language)
        }.body()
    }

    /**
     * Searches for movies by query string.
     *
     * @param query The search query.
     * @param page The page number of results (default is 1).
     * @param language Language code for the response.
     * @param includeAdult Whether to include adult content in the results.
     * @return [Movies] object with the search results. Returns empty list on failure.
     */
    suspend fun searchMovies(
        query: String,
        page: Int = 1,
        language: String,
        includeAdult: Boolean
    ): Movies {
        return try {
            val response: Movies = KtorClient.client.get("$BASE_URL/search/movie") {
                parameter("api_key", API_KEY)
                parameter("query", query)
                parameter("include_adult", includeAdult)
                parameter("language", language)
                parameter("page", page.toString())
            }.body()

            Movies(response.results)
        } catch (e: Exception) {
            Movies(mutableListOf())
        }
    }

    /**
     * Discovers movies by a specific genre.
     *
     * @param genreId ID of the genre to filter by.
     * @param page Page number for pagination (default is 1).
     * @param sortBy Criteria to sort the results (e.g., "popularity.desc").
     * @param includeAdult Whether to include adult content.
     * @param language Language code for the response.
     * @return [Movies] object with discovered movies. Returns empty list on failure.
     */
    suspend fun discoverByGenre(
        genreId: Int,
        page: Int = 1,
        sortBy: String = "popularity.desc",
        includeAdult: Boolean,
        language: String
    ): Movies {
        return try {
            KtorClient.client.get("$BASE_URL/discover/movie") {
                parameter("api_key", API_KEY)
                parameter("language", language)
                parameter("include_adult", includeAdult)
                parameter("page", page)
                parameter("sort_by", sortBy)
                parameter("with_genres", genreId)
            }.body()
        } catch (e: Exception) {
            Movies(mutableListOf())
        }
    }

    /**
     * Fetches movies similar to a given movie.
     *
     * @param movieId The ID of the movie to find similar ones to.
     * @param page Page number for pagination.
     * @param language Language code for the response.
     * @return [Movies] object with similar movie suggestions.
     */
    suspend fun fetchSimilarMovies(
        movieId: Int,
        page: Int = 1,
        language: String
    ): Movies {
        return KtorClient.client.get("$BASE_URL/movie/$movieId/similar") {
            parameter("api_key", API_KEY)
            parameter("language", language)
            parameter("page", page)
        }.body()
    }
}
