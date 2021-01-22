package academy.android.mymovie.viewmodel

import academy.android.mymovie.data.Movie
import academy.android.mymovie.data.getMovieById
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailsViewModel(movieId: Int, application: Application) : AndroidViewModel(application) {

    private val _currentMovie = MutableLiveData<Movie>()
    private val _isLoading = MutableLiveData<Boolean>()

    val currentMovie: LiveData<Movie> = _currentMovie
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            _isLoading.postValue(true)

            //delay to see whether `isLoading` LiveData is working properly or not
            delay(2000)

            _currentMovie.postValue(getMovieById(movieId, getApplication()))

            _isLoading.postValue((false))
        }
    }
}