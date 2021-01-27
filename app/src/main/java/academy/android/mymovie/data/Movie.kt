package academy.android.mymovie.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("imdb_id")
    val imdbId: String?,
    val video: Boolean,
    val title: String,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    val revenue: Int,
    val genres: List<GenresItem>,
    val popularity: Double,
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountriesItem>,
    val id: Int,
    @SerialName("vote_count")
    val voteCount: Int,
    val budget: Int,
    val overview: String?,
    @SerialName("original_title")
    val originalTitle: String,
    val runtime: Int?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("spoken_languages")
    val spokenLanguages: List<SpokenLanguagesItem>,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompaniesItem>,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("vote_average")
    val voteAverage: Float,
//    val belongsToCollection: Any,
    val tagline: String?,
    val adult: Boolean,
    val homepage: String?,
    val status: String,
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
        var certification = "-"
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
data class SpokenLanguagesItem(
    val name: String,
    @SerialName("iso_639_1")
    val iso6391: String,
    @SerialName("english_name")
    val englishName: String
)

@Serializable
data class ProductionCountriesItem(
    @SerialName("iso_3166_1")
    val iso31661: String,
    val name: String
)

@Serializable
data class ProductionCompaniesItem(
    @SerialName("logo_path")
    val logoPath: String?,
    val name: String,
    val id: Int,
    @SerialName("origin_country")
    val originCountry: String
)

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

