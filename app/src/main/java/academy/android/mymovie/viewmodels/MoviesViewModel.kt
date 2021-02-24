package academy.android.mymovie.viewmodels

import academy.android.mymovie.data.Repository
import academy.android.mymovie.utils.ConfigurationService
import academy.android.mymovie.utils.Constants.KEY_POPULAR
import androidx.lifecycle.*
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MoviesViewModel(
    repository: Repository,
    requestPath: String,
    configurationService: ConfigurationService
) : ViewModel() {

    private val currentRequestPath = MutableLiveData(KEY_POPULAR)

    val posterUrl = MutableLiveData(configurationService.getPosterUrl())

    val moviesList = currentRequestPath.switchMap {
        repository.getMoviesList(it).cachedIn(viewModelScope)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            currentRequestPath.postValue(requestPath)
            configurationService.saveConfiguration(repository.getConfiguration())
        }
    }
}