package academy.android.mymovie.api

import academy.android.mymovie.data.Movie
import academy.android.mymovie.data.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("movie/{id}")
    suspend fun getMovieById(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): Movie
}