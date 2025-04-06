package iut.s4.sae.network

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import iut.s4.sae.model.Movies

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
    private val API_KEY = ""

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
            Log.e("MoviesData", "Error searching movies: ${e.message}")
            return Movies(emptyList())
        }
    }
}