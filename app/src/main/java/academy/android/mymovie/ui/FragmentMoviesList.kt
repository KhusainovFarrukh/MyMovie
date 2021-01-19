package academy.android.mymovie.ui

import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.R
import academy.android.mymovie.adapter.MovieAdapter
import academy.android.mymovie.data.loadMovies
import academy.android.mymovie.decorator.MovieItemDecoration
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class FragmentMoviesList : Fragment() {

    private var movieClickInterface: MovieClickInterface? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Default + Job())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.addItemDecoration(
            MovieItemDecoration(
                resources.getDimension(R.dimen.dp8).toInt(),
                resources.getDimension(R.dimen.dp18).toInt()
            )
        )

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        val adapter = MovieAdapter(movieClickInterface!!)
        recyclerView.adapter = adapter

        coroutineScope.launch {
            val moviesList = withContext(coroutineScope.coroutineContext) {
                loadMovies(requireContext())
            }
            adapter.submitList(moviesList)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieClickInterface) {
            movieClickInterface = context
        } else {
            throw IllegalArgumentException("Activity is not MovieClickInterface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        movieClickInterface = null
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.cancel()
    }
}