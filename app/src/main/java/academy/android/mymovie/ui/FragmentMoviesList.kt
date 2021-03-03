package academy.android.mymovie.ui

import academy.android.mymovie.MyMovieApp
import academy.android.mymovie.R
import academy.android.mymovie.adapters.ListLoadStateAdapter
import academy.android.mymovie.adapters.MovieAdapter
import academy.android.mymovie.adapters.MovieAdapter.Companion.VIEW_TYPE_LOADING
import academy.android.mymovie.adapters.MovieAdapter.Companion.VIEW_TYPE_MOVIE
import academy.android.mymovie.api.RetrofitInstance
import academy.android.mymovie.clickinterfaces.MovieClickInterface
import academy.android.mymovie.data.Repository
import academy.android.mymovie.databinding.FragmentMoviesListBinding
import academy.android.mymovie.decorators.MovieItemDecoration
import academy.android.mymovie.utils.Constants.DEFAULT_IMAGE_URL
import academy.android.mymovie.utils.Constants.DEFAULT_SIZE
import academy.android.mymovie.utils.Constants.KEY_POPULAR
import academy.android.mymovie.utils.Constants.REQUEST_PATH
import academy.android.mymovie.viewmodelfactories.MoviesViewModelFactory
import academy.android.mymovie.viewmodels.MoviesViewModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class FragmentMoviesList : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MovieAdapter
    private lateinit var moviesViewModel: MoviesViewModel
    private var movieClickInterface: MovieClickInterface? = null
    private var posterUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = MovieAdapter(movieClickInterface!!, posterUrl ?: DEFAULT_IMAGE_URL + DEFAULT_SIZE)

        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    VIEW_TYPE_MOVIE -> 1
                    VIEW_TYPE_LOADING -> 2
                    else -> throw IllegalArgumentException()
                }
            }
        }

        binding.rvMovies.setHasFixedSize(true)
        binding.rvMovies.layoutManager = gridLayoutManager
        binding.rvMovies.adapter = adapter.withCustomLoadStateHeaderAndFooter(
            ListLoadStateAdapter { adapter.retry() },
            ListLoadStateAdapter { adapter.retry() }
        )
        binding.rvMovies.addItemDecoration(
            MovieItemDecoration(
                resources.getDimension(R.dimen.dp8).toInt(),
                resources.getDimension(R.dimen.dp8).toInt()
            )
        )

        moviesViewModel = ViewModelProvider(
            this,
            MoviesViewModelFactory(
                Repository(RetrofitInstance.movieApi),
                arguments?.getString(REQUEST_PATH, KEY_POPULAR) ?: KEY_POPULAR,
                (requireActivity().application as MyMovieApp).configurationService
            )
        ).get(MoviesViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        moviesViewModel.moviesList.observe(this.viewLifecycleOwner, {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        })
        moviesViewModel.posterUrl.observe(viewLifecycleOwner) {
            posterUrl = it
        }
        moviesViewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireActivity(),
                it,
                Toast.LENGTH_SHORT
            ).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun newInstance(requestPath: String): FragmentMoviesList {
        val fragment = FragmentMoviesList()
        val bundle = Bundle()
        bundle.putString(REQUEST_PATH, requestPath)
        fragment.arguments = bundle
        return fragment
    }
}