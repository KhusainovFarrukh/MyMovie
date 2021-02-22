package academy.android.mymovie.viewmodelfactorys

import academy.android.mymovie.data.Repository
import academy.android.mymovie.viewmodels.DetailsViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailsViewModelFactory(private val repository: Repository, private val movieId: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsViewModel(repository, movieId) as T
    }
}