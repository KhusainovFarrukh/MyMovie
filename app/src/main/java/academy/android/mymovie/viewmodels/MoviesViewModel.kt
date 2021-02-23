package academy.android.mymovie.viewmodels

import academy.android.mymovie.data.Repository
import academy.android.mymovie.utils.ConfigurationService
import academy.android.mymovie.utils.Constants.KEY_BASE_URL
import academy.android.mymovie.utils.Constants.KEY_POPULAR
import academy.android.mymovie.utils.Constants.KEY_POSTER
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MoviesViewModel(repository: Repository, requestPath: String) : ViewModel() {

    private val currentRequestPath = MutableLiveData(KEY_POPULAR)

    val moviesList = currentRequestPath.switchMap {
        repository.getMoviesList(it).cachedIn(viewModelScope)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            currentRequestPath.postValue(requestPath)
            ConfigurationService.saveConfiguration(repository.getConfiguration())
        }
    }

    fun getPosterUrl() = ConfigurationService.getPosterUrl()
}