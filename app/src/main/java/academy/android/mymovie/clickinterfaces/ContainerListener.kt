package academy.android.mymovie.clickinterfaces

import androidx.appcompat.widget.SearchView


interface ContainerListener {
    fun addSearchFragment(searchView: SearchView)
    fun onSearchClosed()
    fun closeSearchView()
}