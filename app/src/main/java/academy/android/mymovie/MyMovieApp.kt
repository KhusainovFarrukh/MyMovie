package academy.android.mymovie

import academy.android.mymovie.utils.ConfigurationService
import android.app.Application
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MyMovieApp : Application() {

    lateinit var configurationService: ConfigurationService

    override fun onCreate() {
        super.onCreate()

        configurationService = ConfigurationService(this)
    }
}