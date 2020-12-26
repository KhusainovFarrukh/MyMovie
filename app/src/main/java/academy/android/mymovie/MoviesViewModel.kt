package academy.android.mymovie

import academy.android.mymovie.data.Movie
import academy.android.mymovie.data.loadMovies
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MoviesViewModel(application: Application) : AndroidViewModel(application){

    private val _moviesList = MutableLiveData<List<Movie>>(emptyList())
    private val _isLoading = MutableLiveData<Boolean>(false)

    val moviesList: LiveData<List<Movie>> = _moviesList
    val isLoading: LiveData<Boolean> = _isLoading

    fun getMovies() {
        viewModelScope.launch {
            _isLoading.postValue(true)

            val tempList = loadMovies(getApplication<Application>().applicationContext)
            _moviesList.postValue(tempList)

            _isLoading.postValue(false)
        }
    }
}