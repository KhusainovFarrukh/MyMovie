package academy.android.mymovie.viewmodel

import academy.android.mymovie.api.Repo
import academy.android.mymovie.data.Movie
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(movieId: Int, application: Application) : AndroidViewModel(application) {

    private val _currentMovie = MutableLiveData<Movie>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val currentMovie: LiveData<Movie> = _currentMovie
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        coroutineScope.launch {
            _isLoading.postValue(true)

            _currentMovie.postValue(Repo.getMovieById(movieId))

            _isLoading.postValue((false))
        }
    }
}