package academy.android.mymovie.viewmodelfactory

import academy.android.mymovie.viewmodel.MoviesViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MoviesViewModelFactory(private val requestPath: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesViewModel(requestPath) as T
    }
}