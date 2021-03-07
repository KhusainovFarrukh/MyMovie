package academy.android.mymovie.viewmodelfactories

import academy.android.mymovie.data.Repository
import academy.android.mymovie.utils.ConfigurationService
import academy.android.mymovie.viewmodels.ActorViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ActorViewModelFactory(
    private val repository: Repository,
    private val actorId: Int,
    private val configurationService: ConfigurationService
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ActorViewModel(repository, actorId, configurationService) as T
    }
}