package academy.android.mymovie.viewmodelfactories

import academy.android.mymovie.data.Repository
import academy.android.mymovie.utils.ConfigurationService
import academy.android.mymovie.viewmodels.DetailsViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailsViewModelFactory(
    private val repository: Repository,
    private val movieId: Int,
    private val configurationService: ConfigurationService
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsViewModel(repository, movieId, configurationService) as T
    }
}