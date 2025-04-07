package iut.s4.sae.network

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import iut.s4.sae.model.Genres
import iut.s4.sae.model.Movie
import iut.s4.sae.model.Movies
import iut.s4.sae.BuildConfig


class MovieDao private constructor(){
    companion object{
        @Volatile
        private var instance : MovieDao?=null

        fun getInstance(): MovieDao {
            return instance ?: synchronized(this){
                instance ?: MovieDao().also { instance = it }
            }
        }
    }
    private val BASE_URL = "https://api.themoviedb.org/3"
    private val API_KEY = BuildConfig.API_KEY
    private val LANGUAGE = "fr"

    suspend fun fetchGenres(): Genres {
        val response: Genres = KtorClient.client.get("$BASE_URL/genre/movie/list") {
            parameter("api_key", API_KEY)
            parameter("language", LANGUAGE)
        }.body()
        return response
    }

    suspend fun fetchTrendingMovies(timeWindow: String = "day"): Movies {
        val response: Movies = KtorClient.client.get("$BASE_URL/trending/movie/$timeWindow") {
            parameter("api_key", API_KEY)
            parameter("language", LANGUAGE)
        }.body()
        return response
    }

    suspend fun searchMovieDetails(movieId: String): Movie {
        val url = "$BASE_URL/movie/$movieId"
        val response: Movie = KtorClient.client.get(url) {
            parameter("api_key", API_KEY)
            parameter("language", LANGUAGE)
        }.body()
        return response
    }

    suspend fun searchMovies(query: String, page : Int = 1): Movies {
        try {
            val response: Movies = KtorClient.client.get("$BASE_URL/search/movie") {
                parameter("api_key", API_KEY)
                parameter("query", query)
                parameter("include_adult", false)
                parameter("language", "en-US")
                parameter("page", page.toString())
            }.body()

            return Movies(response.results)
        } catch (e: Exception) {
            return Movies(emptyList())
        }
    }

    suspend fun discoverByGenre(
        genreId: Int,
        page: Int = 1,
        sortBy: String = "popularity.desc",
        includeAdult: Boolean = false
    ): Movies {
        val response: Movies = KtorClient.client.get("$BASE_URL/discover/movie") {
            parameter("api_key", API_KEY)
            parameter("language", LANGUAGE)
            parameter("include_adult", includeAdult)
            parameter("page", page)
            parameter("sort_by", sortBy)
            parameter("with_genres", genreId)
        }.body()
        return response
    }

}