package academy.android.mymovie.api

import academy.android.mymovie.data.Movie

object Repo {

    private val api = RetrofitInstance.movieApi

    suspend fun getMoviesList(type: String): List<Movie> {
        val movieList = mutableListOf<Movie>()

        api.getMoviesList(type).results.forEach {
            movieList.add(getMovieById(it.id))
        }

        return movieList
    }

    suspend fun getMovieById(id: Int) = api.getMovieById(id)

    suspend fun getCastByMovieId(movieId: Int) = api.getCastByMovieId(movieId).cast

    suspend fun getConfiguration() = api.getConfiguration()

    suspend fun getActorById(actorId: Int) = api.getActorById(actorId)

    suspend fun searchMovie(searchText: String): List<Movie> {
        val movieList = mutableListOf<Movie>()

        api.searchMovie(searchText).results.forEach {
            movieList.add(getMovieById(it.id))
        }

        return movieList
    }
}