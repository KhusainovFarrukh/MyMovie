package academy.android.mymovie.viewmodels

import academy.android.mymovie.data.Repository
import academy.android.mymovie.models.ActorResponse
import academy.android.mymovie.models.DataWrapper
import academy.android.mymovie.utils.ConfigurationService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class ActorViewModel(
    repository: Repository,
    actorId: Int,
    configurationService: ConfigurationService
) : ViewModel() {

    private val _currentActor = MutableLiveData<ActorResponse>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val _errorMessage = MutableLiveData<String>()

    val currentActor: LiveData<ActorResponse> = _currentActor
    val isLoading: LiveData<Boolean> = _isLoading
    val errorMessage: LiveData<String> = _errorMessage

    val backdropUrl = MutableLiveData(configurationService.getPosterUrl())

    init {
        coroutineScope.launch {
            _isLoading.postValue(true)
            repository.getActorById(actorId).let {
                when (it) {
                    is DataWrapper.Success -> _currentActor.postValue(it.data)
                    is DataWrapper.Error -> _errorMessage.postValue(it.message)
                }
            }
            _isLoading.postValue((false))
        }
    }
}
