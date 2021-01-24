package academy.android.mymovie.api

import academy.android.mymovie.data.Actor
import academy.android.mymovie.data.Movie
import academy.android.mymovie.utils.Constants.API_KEY

object Repo {

    private val api = RetrofitInstance.movieApi

    suspend fun getMoviesList(type: String): List<Movie> {
        val movieList = mutableListOf<Movie>()

        api.getMoviesList(type, API_KEY).results.forEach {
            movieList.add(getMovieById(it.id))
        }

        return movieList
    }

    suspend fun getMovieById(id: Int): Movie {
        return api.getMovieById(id, API_KEY)
    }

    suspend fun getCastByMovieId(movieId: Int): List<Actor> {
        return api.getCastByMovieId(movieId, API_KEY).cast
    }
}