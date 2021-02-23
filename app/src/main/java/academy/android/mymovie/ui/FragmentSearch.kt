package academy.android.mymovie.ui

import academy.android.mymovie.R
import academy.android.mymovie.adapters.ListLoadStateAdapter
import academy.android.mymovie.adapters.MovieAdapter
import academy.android.mymovie.api.RetrofitInstance
import academy.android.mymovie.clickinterfaces.MovieClickInterface
import academy.android.mymovie.data.Repository
import academy.android.mymovie.databinding.FragmentMoviesListBinding
import academy.android.mymovie.decorators.MovieItemDecoration
import academy.android.mymovie.utils.Constants.DEFAULT_IMAGE_URL
import academy.android.mymovie.utils.Constants.DEFAULT_SIZE
import academy.android.mymovie.utils.Constants.KEY_BASE_URL
import academy.android.mymovie.utils.Constants.KEY_POSTER
import academy.android.mymovie.utils.Constants.KEY_SHARED_PREF
import academy.android.mymovie.viewmodelfactories.SearchViewModelFactory
import academy.android.mymovie.viewmodels.SearchViewModel
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class FragmentSearch : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MovieAdapter
    private val searchViewModel by activityViewModels<SearchViewModel> {
        SearchViewModelFactory(Repository(RetrofitInstance.movieApi))
    }
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

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.rvMovies.addItemDecoration(
            MovieItemDecoration(
                resources.getDimension(R.dimen.dp8).toInt(),
                resources.getDimension(R.dimen.dp8).toInt()
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
                return when (adapter.getItemViewType(position)) {
                    MovieAdapter.VIEW_TYPE_MOVIE -> 1
                    MovieAdapter.VIEW_TYPE_LOADING -> 2
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
    }

    override fun onStart() {
        super.onStart()

//        searchViewModel = ViewModelProvider(
//            this, SearchViewModelFactory(
//                Repository(RetrofitInstance.movieApi),
//                arguments?.getString(KEY_SEARCH, DEFAULT_SEARCH) ?: DEFAULT_SEARCH
//            )
//        ).get(SearchViewModel::class.java)

        searchViewModel.moviesList.observe(this.viewLifecycleOwner, {
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
}