package academy.android.mymovie.viewmodels

import academy.android.mymovie.data.Repository
import academy.android.mymovie.models.ConfigurationResponse
import academy.android.mymovie.models.ConfigurationResponseWrapper
import academy.android.mymovie.utils.ConfigurationService
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

    private val currentRequestPath = MutableLiveData<String>()
    private val _errorMessage = MutableLiveData<String>()

    val posterUrl = MutableLiveData(configurationService.getPosterUrl())
    val errorMessage: LiveData<String> = _errorMessage
    val moviesList = currentRequestPath.switchMap {
        repository.getMoviesList(it).cachedIn(viewModelScope)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            currentRequestPath.postValue(requestPath)
            repository.getConfiguration().let {
                when (it) {
                    is ConfigurationResponseWrapper.Success -> configurationService.saveConfiguration(it.data)
                    is ConfigurationResponseWrapper.Error -> _errorMessage.postValue(it.message)
                }
            }
        }
    }
}