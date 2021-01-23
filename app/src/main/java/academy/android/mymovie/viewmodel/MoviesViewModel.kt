package academy.android.mymovie.viewmodel

import academy.android.mymovie.api.Repo
import academy.android.mymovie.data.Movie
import academy.android.mymovie.data.MovieInList
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val _moviesList = MutableLiveData<List<Movie>>(emptyList())
    private val _isLoading = MutableLiveData(false)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val moviesList: LiveData<List<Movie>> = _moviesList
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        coroutineScope.launch {
            _isLoading.postValue(true)

            _moviesList.postValue(Repo.getPopularMovies())

            _isLoading.postValue(false)
        }
    }
}