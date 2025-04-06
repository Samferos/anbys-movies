package iut.s4.sae.network

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
}