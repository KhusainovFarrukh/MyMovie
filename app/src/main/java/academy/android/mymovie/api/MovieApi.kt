package academy.android.mymovie.api

import academy.android.mymovie.data.CastResponse
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

    @GET("movie/{movieId}")
    suspend fun getMovieById(
        @Path("movieId") id: Int,
        @Query("api_key") apiKey: String
    ): Movie

    @GET("movie/{movieId}/credits")
    suspend fun getCastByMovieId(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String
    ): CastResponse
}