package academy.android.mymovie

import academy.android.mymovie.data.Movie
import academy.android.mymovie.data.loadMovies
import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class MoviesViewModel(application: Application) : AndroidViewModel(application){

    private val context = getApplication<Application>().applicationContext
    private val _moviesList = MutableLiveData<List<Movie>>(emptyList())
    private val _isLoading = MutableLiveData<Boolean>(false)

    val moviesList: LiveData<List<Movie>> = _moviesList
    val isLoading: LiveData<Boolean> = _isLoading

    fun getMovies() {
        viewModelScope.launch {
            _isLoading.postValue(true)

            val tempList = loadMovies(context)
            _moviesList.postValue(tempList)

            _isLoading.postValue(false)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MoviesFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) return MoviesViewModel(app) as T
        throw IllegalArgumentException("Unable to construct ViewModel")
    }
}