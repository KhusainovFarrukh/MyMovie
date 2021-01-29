package academy.android.mymovie.api

import academy.android.mymovie.data.Movie
import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 *Created by FarrukhKhusainov on 1/29/21 11:57 PM
 **/

private const val PAGE_STARTING_INDEX = 1

class MoviePagingSource(
    private val repository: Repo,
    private val typeOfList: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: PAGE_STARTING_INDEX

        return try {
            val movies = repository.getMoviesList(typeOfList, position)

            LoadResult.Page(
                data = movies,
                prevKey = if (position == PAGE_STARTING_INDEX) null else position - 1,
                nextKey = if (movies.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}