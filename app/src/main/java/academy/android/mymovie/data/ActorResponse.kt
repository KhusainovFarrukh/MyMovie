package academy.android.mymovie.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorResponse(
    val id: Int,
    val name: String,
    @SerialName("known_for_department")
    val knownFor: String,
    val birthday: String?,
    val deathday: String?,
    @SerialName("place_of_birth")
    val birthPlace: String?,
    @SerialName("profile_path")
    val imageUrl: String?,
    val biography: String
)