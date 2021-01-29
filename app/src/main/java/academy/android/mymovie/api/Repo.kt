package academy.android.mymovie.api

import academy.android.mymovie.data.Movie
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData

class Repo {

    private val api = RetrofitInstance.movieApi

    suspend fun getMoviesList(type: String, page: Int): List<Movie> {
        val movieList = mutableListOf<Movie>()

        api.getMoviesList(type, page).results.forEach {
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

    fun getMoviesListNew(type: String): LiveData<PagingData<Movie>> {
        return Pager(
            PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(Repo(), type) }
        ).liveData
    }
}