package academy.android.mymovie.ui

import academy.android.mymovie.MyMovieApp
import academy.android.mymovie.R
import academy.android.mymovie.adapters.ListLoadStateAdapter
import academy.android.mymovie.adapters.MovieAdapter
import academy.android.mymovie.api.RetrofitInstance
import academy.android.mymovie.clickinterfaces.ContainerListener
import academy.android.mymovie.clickinterfaces.MovieClickInterface
import academy.android.mymovie.data.Repository
import academy.android.mymovie.databinding.FragmentSearchBinding
import academy.android.mymovie.decorators.MovieItemDecoration
import academy.android.mymovie.utils.Constants.DEFAULT_IMAGE_URL
import academy.android.mymovie.utils.Constants.DEFAULT_SIZE
import academy.android.mymovie.viewmodelfactories.SearchViewModelFactory
import academy.android.mymovie.viewmodels.SearchViewModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class FragmentSearch : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MovieAdapter
    private lateinit var searchViewModel: SearchViewModel
    private var containerListener: ContainerListener? = null
    private var movieClickInterface: MovieClickInterface? = null
    private var posterUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchViewModel = ViewModelProvider(
            this, SearchViewModelFactory(
                Repository(RetrofitInstance.movieApi),
                (requireActivity().application as MyMovieApp).configurationService
            )
        ).get(SearchViewModel::class.java)

        searchViewModel.posterUrl.observe(viewLifecycleOwner) {
            posterUrl = it
        }

        binding.rvMovies.addItemDecoration(
            MovieItemDecoration(
                resources.getDimension(R.dimen.dp8).toInt(),
                resources.getDimension(R.dimen.dp8).toInt()
            )
        )

        adapter = MovieAdapter(movieClickInterface!!, posterUrl ?: DEFAULT_IMAGE_URL + DEFAULT_SIZE)

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

        binding.svSearch.setOnCloseListener {
            containerListener?.onSearchClosed()
            false
        }

        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewModel.search(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    override fun onStart() {
        super.onStart()

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
        if (context is ContainerListener) {
            containerListener = context
        } else {
            throw IllegalArgumentException("$context is not ContainerListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        movieClickInterface = null
        containerListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}