package academy.android.mymovie.models

import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    val page: Int,
    val results: List<MovieInList>,
    val total_pages: Int,
    val total_results: Int
)

@Serializable
data class MovieInList(
    val id: Int
)