package academy.android.mymovie.models

/**
 *Created by FarrukhKhusainov on 3/3/21 3:27 PM
 **/
sealed class ConfigurationResponseWrapper {
    data class Success(val data: ConfigurationResponse) : ConfigurationResponseWrapper()
    data class Error(val message: String) : ConfigurationResponseWrapper()
}
