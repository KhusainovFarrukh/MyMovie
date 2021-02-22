package academy.android.mymovie.data

import academy.android.mymovie.api.MovieApi
import academy.android.mymovie.models.Movie
import androidx.paging.PagingSource

/**
 *Created by FarrukhKhusainov on 1/29/21 11:57 PM
 **/

private const val PAGE_STARTING_INDEX = 1

class MoviePagingSource(
    private val movieApi: MovieApi,
    private val typeOfList: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: PAGE_STARTING_INDEX

        return try {
            val movieList = mutableListOf<Movie>()

            movieApi.getMoviesList(typeOfList, position).results.forEach {
                movieList.add(movieApi.getMovieById(it.id))
            }

            LoadResult.Page(
                data = movieList,
                prevKey = if (position == PAGE_STARTING_INDEX) null else position - 1,
                nextKey = if (movieList.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            LoadResult.Error(exception)
        }
    }
}

class SearchPagingSource(
    private val movieApi: MovieApi,
    private val searchText: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: PAGE_STARTING_INDEX

        return try {
            val movieList = mutableListOf<Movie>()

            movieApi.searchMovie(searchText, position).results.forEach {
                movieList.add(movieApi.getMovieById(it.id))
            }

            LoadResult.Page(
                data = movieList,
                prevKey = if (position == PAGE_STARTING_INDEX) null else position - 1,
                nextKey = if (movieList.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            LoadResult.Error(exception)
        }
    }
}