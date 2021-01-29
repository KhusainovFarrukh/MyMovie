package academy.android.mymovie.viewmodel

import academy.android.mymovie.api.Repo
import academy.android.mymovie.data.ConfigurationResponse
import academy.android.mymovie.utils.Constants.KEY_POPULAR
import androidx.lifecycle.*
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesViewModel(repository: Repo, requestPath: String) : ViewModel() {

    private val currentRequestPath = MutableLiveData(KEY_POPULAR)

    val moviesList = currentRequestPath.switchMap {
        repository.getMoviesListNew(it).cachedIn(viewModelScope)
    }

    private val _isLoading = MutableLiveData(false)
    private val _config = MutableLiveData<ConfigurationResponse>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val isLoading: LiveData<Boolean> = _isLoading
    val config: LiveData<ConfigurationResponse> = _config

    init {
        coroutineScope.launch {
            _config.postValue(repository.getConfiguration())
        }
        coroutineScope.launch {
            _isLoading.postValue(true)

            currentRequestPath.postValue(requestPath)

            _isLoading.postValue(false)
        }
    }
}