package academy.android.mymovie.ui

import academy.android.mymovie.R
import academy.android.mymovie.adapter.ActorAdapter
import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.data.Actor
import academy.android.mymovie.data.Movie
import academy.android.mymovie.databinding.FragmentMoviesDetailsBinding
import academy.android.mymovie.decorator.ActorItemDecoration
import academy.android.mymovie.ui.MainActivity.Companion.MOVIE_KEY
import academy.android.mymovie.utils.Constants
import academy.android.mymovie.utils.Constants.DEFAULT_IMAGE_URL
import academy.android.mymovie.utils.Constants.KEY_BACKDROP
import academy.android.mymovie.utils.Constants.KEY_BASE_URL
import academy.android.mymovie.utils.Constants.KEY_PROFILE
import academy.android.mymovie.utils.Constants.KEY_SHARED_PREF
import academy.android.mymovie.viewmodel.DetailsViewModel
import academy.android.mymovie.viewmodelfactory.DetailsViewModelFactory
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FragmentMoviesDetails : Fragment() {
    private var _binding: FragmentMoviesDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var adapter: ActorAdapter
    private lateinit var backdropUrl: String
    private var movieClickInterface: MovieClickInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPrefs =
            requireActivity().getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE)

        val imageUrl = sharedPrefs.getString(KEY_BASE_URL, DEFAULT_IMAGE_URL) +
                sharedPrefs.getString(KEY_PROFILE, Constants.DEFAULT_SIZE)

        adapter = ActorAdapter(imageUrl)

        backdropUrl = sharedPrefs.getString(KEY_BASE_URL, DEFAULT_IMAGE_URL) +
                sharedPrefs.getString(KEY_BACKDROP, Constants.DEFAULT_SIZE)
        setupViews()
    }

    override fun onStart() {
        super.onStart()

        detailsViewModel = ViewModelProvider(
            this, DetailsViewModelFactory(
                arguments?.getInt(MOVIE_KEY)
                    ?: throw NullPointerException("No id for current movie"),
                requireActivity().application
            )
        ).get(DetailsViewModel::class.java)

        detailsViewModel.currentMovie.observe(this, this::updateView)
        detailsViewModel.isLoading.observe(this, this::setLoading)
        detailsViewModel.isLoadingActors.observe(this, this::setLoadingActors)
        detailsViewModel.actorsList.observe(this, this::setActorsViews)
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

    private fun updateView(currentMovie: Movie) {

        currentMovie.genres.forEach {
            binding.txvGenres.append(it.name)
            if (it != currentMovie.genres.last()) {
                binding.txvGenres.append(", ")
            }
        }

        binding.txvTitle.text = currentMovie.title
        binding.txvOverview.text = currentMovie.overview
        binding.txvAge.text =
            getString(R.string.age, if (currentMovie.adult) 16 else 13)
        binding.txvReviews.text =
            getString(R.string.reviews, currentMovie.voteCount)
        binding.rbRating.rating =
            currentMovie.voteAverage / 2
        Glide.with(requireActivity())
            .load(backdropUrl + currentMovie.backdropPath)
            .apply(imageOption)
            .into(binding.imvBackdrop)
    }

    private fun setupViews() {
        binding.rvActors.addItemDecoration(
            ActorItemDecoration(
                resources.getDimension(R.dimen.dp8)
                    .toInt()
            )
        )

        binding.rvActors.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvActors.adapter = adapter

        binding.txvBack.apply {
            setOnClickListener {
                movieClickInterface?.onBackClick()
            }
        }
    }

    private fun setActorsViews(actors: List<Actor>) {
//        if data about movie doesn`t contain list of actors, don`t show 'Cast' text
        if (actors.isEmpty()) {
            binding.txvCast.visibility =
                View.INVISIBLE
        } else {
            binding.txvCast.visibility =
                View.VISIBLE
            adapter.submitList(actors)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.rlProgress.isVisible = isLoading
    }

    private fun setLoadingActors(isLoadingActors: Boolean) {
        binding.prbLoadingActors.isVisible = isLoadingActors
    }

    private companion object {
        val imageOption = RequestOptions()
            .placeholder(R.drawable.sample_placeholder)
            .fallback(R.drawable.sample_placeholder)
    }
}