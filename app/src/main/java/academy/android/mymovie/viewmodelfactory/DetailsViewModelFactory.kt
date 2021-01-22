package academy.android.mymovie.viewmodelfactory

import academy.android.mymovie.viewmodel.DetailsViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailsViewModelFactory(private val movieId: Int, private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsViewModel(movieId, application) as T
    }
}