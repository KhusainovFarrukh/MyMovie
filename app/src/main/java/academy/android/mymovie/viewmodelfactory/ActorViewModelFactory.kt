package academy.android.mymovie.viewmodelfactory

import academy.android.mymovie.data.Repository
import academy.android.mymovie.viewmodel.ActorViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ActorViewModelFactory(private val repository: Repository, private val actorId: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ActorViewModel(repository, actorId) as T
    }
}