package academy.android.mymovie.viewmodel

import academy.android.mymovie.data.Repository
import academy.android.mymovie.utils.Constants.DEFAULT_SEARCH
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn

class SearchViewModel(repository: Repository, searchText: String) : ViewModel() {

    private val currentSearchText = MutableLiveData(DEFAULT_SEARCH)

    val moviesList = currentSearchText.switchMap {
        repository.searchMovie(it).cachedIn(viewModelScope)
    }

    init {
        currentSearchText.postValue(searchText)
    }
}