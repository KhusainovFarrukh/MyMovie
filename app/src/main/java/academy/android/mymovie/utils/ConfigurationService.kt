package academy.android.mymovie.utils

import academy.android.mymovie.models.ConfigurationResponse
import academy.android.mymovie.utils.Constants.DEFAULT_IMAGE_URL
import academy.android.mymovie.utils.Constants.DEFAULT_SIZE
import academy.android.mymovie.utils.Constants.KEY_BACKDROP
import academy.android.mymovie.utils.Constants.KEY_BASE_URL
import academy.android.mymovie.utils.Constants.KEY_POSTER
import academy.android.mymovie.utils.Constants.KEY_PROFILE
import academy.android.mymovie.utils.Constants.KEY_SHARED_PREF
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class ConfigurationService(context: Context) {

    private val sharedPrefs = context.getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE)
    private val editor = sharedPrefs.edit()

    init {
        if (!sharedPrefs.contains(KEY_BASE_URL)) {
            editor.apply {
                putString(KEY_BASE_URL, DEFAULT_IMAGE_URL)
                putString(KEY_POSTER, DEFAULT_SIZE)
                putString(KEY_BACKDROP, DEFAULT_SIZE)
                putString(KEY_PROFILE, DEFAULT_SIZE)
                apply()
            }
        }
    }

    fun saveConfiguration(config: ConfigurationResponse) {
        editor.apply {
            putString(KEY_BASE_URL, config.images.baseUrl)
            putString(KEY_POSTER, config.images.posterSizes.last())
            putString(KEY_BACKDROP, config.images.backdropSizes.last())
            putString(KEY_PROFILE, config.images.profileSizes.last())
            apply()
        }
    }

    fun getPosterUrl() =
        sharedPrefs.getString(KEY_BASE_URL, DEFAULT_IMAGE_URL) + sharedPrefs.getString(
            KEY_POSTER, DEFAULT_SIZE
        )

    fun getProfileUrl() =
        sharedPrefs.getString(KEY_BASE_URL, DEFAULT_IMAGE_URL) + sharedPrefs.getString(
            KEY_PROFILE, DEFAULT_SIZE
        )

    fun getBackdropUrl() =
        sharedPrefs.getString(KEY_BASE_URL, DEFAULT_IMAGE_URL) + sharedPrefs.getString(
            KEY_BACKDROP, DEFAULT_SIZE
        )
}