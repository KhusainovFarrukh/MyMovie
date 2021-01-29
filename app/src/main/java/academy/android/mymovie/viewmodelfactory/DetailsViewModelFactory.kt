package academy.android.mymovie.viewmodelfactory

import academy.android.mymovie.api.Repository
import academy.android.mymovie.viewmodel.DetailsViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailsViewModelFactory(private val repository: Repository, private val movieId: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsViewModel(repository, movieId) as T
    }
}