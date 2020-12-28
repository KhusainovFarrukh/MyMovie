package academy.android.mymovie

import academy.android.mymovie.adapter.MovieAdapter
import academy.android.mymovie.adapter.MovieClickInterface
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

class FragmentMoviesList : Fragment() {

    private lateinit var moviesViewModel: MoviesViewModel
    private val adapter = MovieAdapter()
    private lateinit var prbLoading: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prbLoading = view.findViewById(R.id.prb_loading)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        moviesViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(MoviesViewModel::class.java)

        moviesViewModel.isLoading.observe(this.viewLifecycleOwner, this::setLoading)
        moviesViewModel.moviesList.observe(this.viewLifecycleOwner, adapter::submitList)
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

    private fun setLoading(isLoading: Boolean) {
        prbLoading.isVisible = isLoading
    }
}