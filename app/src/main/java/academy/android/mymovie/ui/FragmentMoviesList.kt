package academy.android.mymovie.ui

import academy.android.mymovie.R
import academy.android.mymovie.adapter.ListLoadStateAdapter
import academy.android.mymovie.adapter.MovieAdapter
import academy.android.mymovie.api.Repository
import academy.android.mymovie.api.RetrofitInstance
import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.data.ConfigurationResponse
import academy.android.mymovie.databinding.FragmentMoviesListBinding
import academy.android.mymovie.decorator.MovieItemDecoration
import academy.android.mymovie.utils.Constants.DEFAULT_IMAGE_URL
import academy.android.mymovie.utils.Constants.DEFAULT_SIZE
import academy.android.mymovie.utils.Constants.KEY_BACKDROP
import academy.android.mymovie.utils.Constants.KEY_BASE_URL
import academy.android.mymovie.utils.Constants.KEY_POPULAR
import academy.android.mymovie.utils.Constants.KEY_POSTER
import academy.android.mymovie.utils.Constants.KEY_PROFILE
import academy.android.mymovie.utils.Constants.KEY_SHARED_PREF
import academy.android.mymovie.utils.Constants.REQUEST_PATH
import academy.android.mymovie.viewmodel.MoviesViewModel
import academy.android.mymovie.viewmodelfactory.MoviesViewModelFactory
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch

class FragmentMoviesList : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MovieAdapter
    private lateinit var moviesViewModel: MoviesViewModel
    private var movieClickInterface: MovieClickInterface? = null
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.rvMovies.addItemDecoration(
            MovieItemDecoration(
                resources.getDimension(R.dimen.dp8).toInt(),
                resources.getDimension(R.dimen.dp18).toInt()
            )
        )

        sharedPrefs = requireActivity().getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE)
        editor = sharedPrefs.edit()

        val imageUrl = sharedPrefs.getString(KEY_BASE_URL, DEFAULT_IMAGE_URL) +
                sharedPrefs.getString(KEY_POSTER, DEFAULT_SIZE)

        adapter = MovieAdapter(movieClickInterface!!, imageUrl)

        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return adapter.getItemViewType(position)
            }
        }

        binding.rvMovies.setHasFixedSize(true)
        binding.rvMovies.layoutManager = gridLayoutManager
        binding.rvMovies.adapter = adapter.withCustomFooter(
            ListLoadStateAdapter { adapter.retry() }
        )
    }

    override fun onStart() {
        super.onStart()

        moviesViewModel = ViewModelProvider(
            this,
            MoviesViewModelFactory(
                Repository(RetrofitInstance.movieApi),
                arguments?.getString(REQUEST_PATH, KEY_POPULAR) ?: KEY_POPULAR
            )
        ).get(MoviesViewModel::class.java)

        moviesViewModel.config.observe(this.viewLifecycleOwner, this::saveConfigData)
        moviesViewModel.moviesList.observe(this.viewLifecycleOwner, {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        })
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

    private fun setLoading(isLoading: Boolean) {
        binding.prbLoading.isVisible = isLoading
    }

    private fun saveConfigData(config: ConfigurationResponse) {
        editor.apply {
            putString(KEY_BASE_URL, config.images.baseUrl)
            putString(KEY_BACKDROP, config.images.backdropSizes.last())
            putString(KEY_POSTER, config.images.posterSizes.last())
            putString(KEY_PROFILE, config.images.profileSizes.last())
            apply()
        }
    }

    fun newInstance(requestPath: String): FragmentMoviesList {
        val fragment = FragmentMoviesList()
        val bundle = Bundle()
        bundle.putString(REQUEST_PATH, requestPath)
        fragment.arguments = bundle
        return fragment
    }

    //function for setting custom footer, because default one not shows loadState on initial request
    private fun MovieAdapter.withCustomFooter(
        footerAdapter: LoadStateAdapter<*>
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            footerAdapter.loadState = when (loadStates.refresh) {
                is LoadState.NotLoading -> loadStates.append
                else -> loadStates.refresh
            }
        }
        return ConcatAdapter(this, footerAdapter)
    }
}