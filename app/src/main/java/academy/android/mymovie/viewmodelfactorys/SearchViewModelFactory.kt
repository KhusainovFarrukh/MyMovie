package academy.android.mymovie.viewmodelfactorys

import academy.android.mymovie.data.Repository
import academy.android.mymovie.viewmodels.SearchViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchViewModelFactory(private val repository: Repository, private val searchText: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(repository, searchText) as T
    }
}