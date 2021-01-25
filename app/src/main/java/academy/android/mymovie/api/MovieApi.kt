package academy.android.mymovie.api

import academy.android.mymovie.data.CastResponse
import academy.android.mymovie.data.ConfigurationResponse
import academy.android.mymovie.data.Movie
import academy.android.mymovie.data.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApi {

    @GET("movie/{type}")
    suspend fun getMoviesList(
        @Path("type") type: String
    ): MovieResponse

    @GET("movie/{movieId}")
    suspend fun getMovieById(
        @Path("movieId") id: Int
    ): Movie

    @GET("movie/{movieId}/credits")
    suspend fun getCastByMovieId(
        @Path("movieId") movieId: Int
    ): CastResponse

    @GET("configuration")
    suspend fun getConfiguration() : ConfigurationResponse
}