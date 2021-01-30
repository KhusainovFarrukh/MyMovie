package academy.android.mymovie.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CastResponse(
    val cast: List<Actor>
)

@Serializable
data class Actor(
    val id: Int,
    val name: String,
    @SerialName("profile_path")
    val profileUrl: String?,
    val character: String
)