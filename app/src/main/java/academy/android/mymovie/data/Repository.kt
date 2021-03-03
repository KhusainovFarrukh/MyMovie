package academy.android.mymovie.data

import academy.android.mymovie.api.MovieApi
import academy.android.mymovie.models.*
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData

class Repository(private val movieApi: MovieApi) {

    suspend fun getMovieById(id: Int): DataWrapper<Movie> {
        return try {
            val data = movieApi.getMovieById(id)
            DataWrapper.Success(data)
        } catch (e: Exception) {
            DataWrapper.Error(e.message.toString())
        }
    }

    suspend fun getCastByMovieId(movieId: Int): DataWrapper<List<Actor>> {
        return try {
            val data = movieApi.getCastByMovieId(movieId).cast
            DataWrapper.Success(data)
        } catch (e: Exception) {
            DataWrapper.Error(e.message.toString())
        }
    }

    suspend fun getConfiguration(): DataWrapper<ConfigurationResponse> {
        return try {
            val data = movieApi.getConfiguration()
            DataWrapper.Success(data)
        } catch (e: Exception) {
            DataWrapper.Error(e.message.toString())
        }
    }

    suspend fun getActorById(actorId: Int): DataWrapper<ActorResponse> {
        return try {
            val data = movieApi.getActorById(actorId)
            DataWrapper.Success(data)
        } catch (e: Exception) {
            DataWrapper.Error(e.message.toString())
        }
    }

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