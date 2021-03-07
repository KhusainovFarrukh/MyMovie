package academy.android.mymovie.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationResponse(
    val images: ImageInConfiguration
)

@Serializable
data class ImageInConfiguration(
    @SerialName("secure_base_url")
    val baseUrl: String,
    @SerialName("backdrop_sizes")
    val backdropSizes: List<String>,
    @SerialName("poster_sizes")
    val posterSizes: List<String>,
    @SerialName("profile_sizes")
    val profileSizes: List<String>
)
