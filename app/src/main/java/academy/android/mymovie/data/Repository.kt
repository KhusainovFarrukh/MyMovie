package academy.android.mymovie.data

import academy.android.mymovie.api.MovieApi
import academy.android.mymovie.models.ConfigurationResponseWrapper
import academy.android.mymovie.models.Movie
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData

class Repository(private val movieApi: MovieApi) {

    suspend fun getMovieById(id: Int) = movieApi.getMovieById(id)

    suspend fun getCastByMovieId(movieId: Int) = movieApi.getCastByMovieId(movieId).cast

    suspend fun getConfiguration(): ConfigurationResponseWrapper {
        return try {
            val data = movieApi.getConfiguration()
            ConfigurationResponseWrapper.Success(data)
        } catch (e: Exception) {
            ConfigurationResponseWrapper.Error(e.message.toString())
        }
    }

    suspend fun getActorById(actorId: Int) = movieApi.getActorById(actorId)

    fun searchMovie(searchText: String): LiveData<PagingData<Movie>> {
        return Pager(
            PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchPagingSource(movieApi, searchText) }
        ).liveData
    }

    fun getMoviesList(type: String): LiveData<PagingData<Movie>> {
        return Pager(
            PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(movieApi, type) }
        ).liveData
    }
}