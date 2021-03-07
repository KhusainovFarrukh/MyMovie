package academy.android.mymovie.models

/**
 *Created by FarrukhKhusainov on 3/3/21 3:27 PM
 **/
sealed class DataWrapper<T: Any> {
    data class Success<T: Any>(val data: T) : DataWrapper<T>()
    data class Error<T: Any>(val message: String) : DataWrapper<T>()
}
