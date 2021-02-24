package academy.android.mymovie.viewmodelfactories

import academy.android.mymovie.data.Repository
import academy.android.mymovie.utils.ConfigurationService
import academy.android.mymovie.viewmodels.MoviesViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MoviesViewModelFactory(
    private val repository: Repository,
    private val requestPath: String,
    private val configurationService: ConfigurationService
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesViewModel(repository, requestPath, configurationService) as T
    }
}