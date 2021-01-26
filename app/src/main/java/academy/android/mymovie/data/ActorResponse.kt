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
    val biography: String,
    @SerialName("movie_credits")
    val filmography: CreditsInActor,
    val images: ProfilesInActor
)

@Serializable
data class CreditsInActor(
    val cast: List<MovieInActor>
)

@Serializable
data class MovieInActor(
    val id: Int,
    val title: String,
    @SerialName("poster_path")
    val imageUrl: String?
)

@Serializable
data class ProfilesInActor(
    val profiles: List<ImageInActor>
)

@Serializable
data class ImageInActor(
    @SerialName("file_path")
    val imageUrl: String
)