package academy.android.mymovie

import academy.android.mymovie.data.Movie
import academy.android.mymovie.data.loadMovies
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val _selectedItem = MutableLiveData<Movie>()

    val selectedItem: LiveData<Movie> = _selectedItem

    fun selectItem(id: Int) {
        viewModelScope.launch {

            val tempList = loadMovies(context)

            tempList.forEach {
                if (id == it.id) {
                    _selectedItem.postValue(it)
                    return@forEach
                }
            }
        }
    }
}