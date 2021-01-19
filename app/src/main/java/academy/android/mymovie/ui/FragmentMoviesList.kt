package academy.android.mymovie.ui

import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.R
import academy.android.mymovie.adapter.MovieAdapter
import academy.android.mymovie.data.loadMovies
import academy.android.mymovie.decorator.MovieItemDecoration
import academy.android.mymovie.viewmodel.MoviesViewModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class FragmentMoviesList : Fragment() {

    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var prbLoading: ProgressBar
    private var movieClickInterface: MovieClickInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prbLoading = view.findViewById(R.id.prb_loading)
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

        moviesViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(MoviesViewModel::class.java)

        moviesViewModel.isLoading.observe(this.viewLifecycleOwner, this::setLoading)
        moviesViewModel.moviesList.observe(this.viewLifecycleOwner, adapter::submitList)

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

    private fun setLoading(isLoading: Boolean) {
        prbLoading.isVisible = isLoading
    }
}