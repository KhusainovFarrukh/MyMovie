package academy.android.mymovie.utils

import academy.android.mymovie.models.ConfigurationResponse
import academy.android.mymovie.utils.Constants.DEFAULT_IMAGE_URL
import academy.android.mymovie.utils.Constants.DEFAULT_SIZE
import academy.android.mymovie.utils.Constants.KEY_BACKDROP
import academy.android.mymovie.utils.Constants.KEY_BASE_URL
import academy.android.mymovie.utils.Constants.KEY_POSTER
import academy.android.mymovie.utils.Constants.KEY_PROFILE
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
object ConfigurationService {

    val configuration = MutableLiveData(HashMap<String, String>())

    init {
        configuration.value!![KEY_BASE_URL] = DEFAULT_IMAGE_URL
        configuration.value!![KEY_POSTER] = DEFAULT_SIZE
        configuration.value!![KEY_BACKDROP] = DEFAULT_SIZE
        configuration.value!![KEY_PROFILE] = DEFAULT_SIZE
    }

    fun saveConfiguration(config: ConfigurationResponse) {

        configuration.value!![KEY_BASE_URL] = config.images.baseUrl
        configuration.value!![KEY_POSTER] = config.images.posterSizes.last()
        configuration.value!![KEY_BACKDROP] = config.images.backdropSizes.last()
        configuration.value!![KEY_PROFILE] = config.images.profileSizes.last()
    }

    fun getPosterUrl() = configuration.value!![KEY_BASE_URL] +
            configuration.value!![KEY_POSTER]

    fun getProfileUrl() = configuration.value!![KEY_BASE_URL] +
            configuration.value!![KEY_PROFILE]

    fun getBackdropUrl() = configuration.value!![KEY_BASE_URL] +
            configuration.value!![KEY_BACKDROP]
}