package academy.android.mymovie.api

import academy.android.mymovie.data.*
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
    suspend fun getConfiguration(): ConfigurationResponse

    @GET("person/{actorId}?append_to_response=images,movie_credits")
    suspend fun getActorById(
        @Path("actorId") actorId: Int
    ): ActorResponse
}