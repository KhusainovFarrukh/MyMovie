package academy.android.mymovie

import academy.android.mymovie.adapter.MovieAdapter
import academy.android.mymovie.adapter.MovieClickInterface
import academy.android.mymovie.data.loadMovies
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

    private val coroutineScope = CoroutineScope(Dispatchers.Default + Job())
    private val adapter = MovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        coroutineScope.launch {
            val moviesList = withContext(coroutineScope.coroutineContext) {
                loadMovies(requireContext())
            }
            adapter.submitList(moviesList)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieClickInterface)
            adapter.movieClickInterface = context
    }

    override fun onDetach() {
        super.onDetach()
        adapter.movieClickInterface = null
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.cancel()
    }
}