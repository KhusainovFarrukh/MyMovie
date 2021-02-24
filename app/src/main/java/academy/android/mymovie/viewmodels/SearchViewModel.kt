package academy.android.mymovie.viewmodels

import academy.android.mymovie.data.Repository
import academy.android.mymovie.utils.ConfigurationService
import academy.android.mymovie.utils.Constants
import academy.android.mymovie.utils.Constants.DEFAULT_SEARCH
import academy.android.mymovie.utils.Constants.KEY_BASE_URL
import academy.android.mymovie.utils.Constants.KEY_POSTER
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class SearchViewModel(
    private val repository: Repository,
    private val configurationService: ConfigurationService
) : ViewModel() {

    private val currentSearchText = MutableLiveData<String>()

    val moviesList = currentSearchText.switchMap {
        repository.searchMovie(it).cachedIn(viewModelScope)
    }

    fun search(searchText: String) {
        currentSearchText.postValue(searchText)
    }

    fun getPosterUrl() = configurationService.getPosterUrl()
}