package academy.android.mymovie.viewmodels

import academy.android.mymovie.data.Repository
import academy.android.mymovie.models.Actor
import academy.android.mymovie.models.DataWrapper
import academy.android.mymovie.models.Movie
import academy.android.mymovie.utils.ConfigurationService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class DetailsViewModel(
    repository: Repository,
    movieId: Int,
    configurationService: ConfigurationService
) : ViewModel() {

    private val _currentMovie = MutableLiveData<Movie>()
    private val _actorsList = MutableLiveData<List<Actor>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isLoadingActors = MutableLiveData<Boolean>()
    private val _errorMessage = MutableLiveData<String>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val currentMovie: LiveData<Movie> = _currentMovie
    val actorsList: LiveData<List<Actor>> = _actorsList
    val isLoading: LiveData<Boolean> = _isLoading
    val isLoadingActors: LiveData<Boolean> = _isLoadingActors
    val errorMessage: LiveData<String> = _errorMessage

    val profileUrl = MutableLiveData(configurationService.getProfileUrl())
    val backdropUrl = MutableLiveData(configurationService.getBackdropUrl())

    init {
        coroutineScope.launch {

            coroutineScope.launch {
                _isLoading.postValue(true)
                repository.getMovieById(movieId).let {
                    when (it) {
                        is DataWrapper.Success -> _currentMovie.postValue(it.data)
                        is DataWrapper.Error -> _errorMessage.postValue(it.message)
                    }
                }
                _isLoading.postValue((false))
            }

            coroutineScope.launch {
                _isLoadingActors.postValue(true)
                repository.getCastByMovieId(movieId).let {
                    when (it) {
                        is DataWrapper.Success -> _actorsList.postValue(it.data)
                        is DataWrapper.Error -> _errorMessage.postValue(it.message)
                    }
                }
                _isLoadingActors.postValue(false)
            }
        }
    }
}