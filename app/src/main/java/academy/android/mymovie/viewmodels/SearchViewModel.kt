package academy.android.mymovie.viewmodels

import academy.android.mymovie.data.Repository
import academy.android.mymovie.utils.Constants.DEFAULT_SEARCH
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val currentSearchText = MutableLiveData<String>()

    val moviesList = currentSearchText.switchMap {
        repository.searchMovie(it).cachedIn(viewModelScope)
    }

    fun search(searchText: String) {
        currentSearchText.postValue(searchText)
    }
}