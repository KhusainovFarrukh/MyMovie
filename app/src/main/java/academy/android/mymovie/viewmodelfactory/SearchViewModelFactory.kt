package academy.android.mymovie.viewmodelfactory

import academy.android.mymovie.viewmodel.SearchViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchViewModelFactory(
    private val requestPath: String,
    private val application: Application
) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(requestPath, application) as T
    }
}