package academy.android.mymovie.viewmodel

import academy.android.mymovie.api.Repo
import academy.android.mymovie.data.ActorResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActorViewModel(private val actorId: Int) : ViewModel() {

    private val _currentActor = MutableLiveData<ActorResponse>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val currentActor: LiveData<ActorResponse> = _currentActor
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        coroutineScope.launch {
            _isLoading.postValue(true)
            _currentActor.postValue(Repo.getActorById(actorId))
            _isLoading.postValue((false))
        }
    }
}
