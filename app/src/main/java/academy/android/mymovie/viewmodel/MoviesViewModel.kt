package academy.android.mymovie.viewmodel

import academy.android.mymovie.data.Movie
import academy.android.mymovie.data.loadMovies
import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val _moviesList = MutableLiveData<List<Movie>>(emptyList())
    private val _isLoading = MutableLiveData(false)

    val moviesList: LiveData<List<Movie>> = _moviesList
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            _isLoading.postValue(true)

            //delay to see whether `isLoading` LiveData is working properly or not
            delay(2000)
            val tempList = loadMovies(getApplication())
            _moviesList.postValue(tempList)

            _isLoading.postValue(false)
        }
    }
}