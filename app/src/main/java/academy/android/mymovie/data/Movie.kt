package academy.android.mymovie.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val title: String,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    val genres: List<GenresItem>,
    val id: Int,
    @SerialName("vote_count")
    val voteCount: Int,
    val overview: String?,
    val runtime: Int?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("vote_average")
    val voteAverage: Float,
    val tagline: String?,
    @SerialName("release_dates")
    val releaseDates: ReleaseDates
) {
    fun getGenres(): String {
        var returnString = ""
        genres.forEach {
            returnString += it.name
            if (it != genres.last()) {
                returnString += ", "
            }
        }
        return returnString
    }

    fun getCertification(): String {
        var certification = ""
        releaseDates.results.forEach { releaseDate ->
            if (releaseDate.countryCode == "US") {
                releaseDate.certificates.forEach { certificate ->
                    if (certificate.certification.isNotEmpty()) {
                        certification = certificate.certification
                    }
                }
            }
        }
        return certification
    }
}

@Serializable
data class GenresItem(
    val name: String,
    val id: Int
)

@Serializable
data class ReleaseDates(
    val results: List<ReleaseDate>
)

@Serializable
data class ReleaseDate(
    @SerialName("iso_3166_1")
    val countryCode: String,
    @SerialName("release_dates")
    val certificates: List<Certificate>
)

@Serializable
data class Certificate(
    val certification: String
)

