package academy.android.mymovie.model

import java.io.Serializable

data class Movie(
    val name: String,
    val time: Int,
    val storyline: String,
    val rating: Float,
    val reviews: Int,
    val tagline: String,
    val age: Int,
    val isFavorite: Boolean,
    val imageUrl: String,
    val bannerUrl: String,
    val actors: ArrayList<Actor>?
    ) : Serializable