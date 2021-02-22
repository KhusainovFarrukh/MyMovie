package academy.android.mymovie.viewmodelfactorys

import academy.android.mymovie.data.Repository
import academy.android.mymovie.viewmodels.MoviesViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MoviesViewModelFactory(private val repository: Repository, private val requestPath: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesViewModel(repository, requestPath) as T
    }
}