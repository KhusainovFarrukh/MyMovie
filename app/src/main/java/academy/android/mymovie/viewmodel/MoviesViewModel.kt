package academy.android.mymovie.viewmodel

import academy.android.mymovie.data.Repository
import academy.android.mymovie.model.ConfigurationResponse
import academy.android.mymovie.utils.Constants.KEY_POPULAR
import androidx.lifecycle.*
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesViewModel(repository: Repository, requestPath: String) : ViewModel() {

    private val currentRequestPath = MutableLiveData(KEY_POPULAR)

    val moviesList = currentRequestPath.switchMap {
        repository.getMoviesList(it).cachedIn(viewModelScope)
    }

    private val _config = MutableLiveData<ConfigurationResponse>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val config: LiveData<ConfigurationResponse> = _config

    init {
        coroutineScope.launch {
            currentRequestPath.postValue(requestPath)
            _config.postValue(repository.getConfiguration())
        }
    }
}