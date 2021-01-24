package academy.android.mymovie.viewmodelfactory

import academy.android.mymovie.viewmodel.MoviesViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MoviesViewModelFactory(
    private val requestPath: String,
    private val application: Application
) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesViewModel(requestPath, application) as T
    }
}