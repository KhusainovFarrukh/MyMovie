package academy.android.mymovie.viewmodels

import academy.android.mymovie.data.Repository
import academy.android.mymovie.models.ActorResponse
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

    val currentActor: LiveData<ActorResponse> = _currentActor
    val isLoading: LiveData<Boolean> = _isLoading

    val backdropUrl = MutableLiveData(configurationService.getPosterUrl())

    init {
        coroutineScope.launch {
            _isLoading.postValue(true)
            _currentActor.postValue(repository.getActorById(actorId))
            _isLoading.postValue((false))
        }
    }
}
