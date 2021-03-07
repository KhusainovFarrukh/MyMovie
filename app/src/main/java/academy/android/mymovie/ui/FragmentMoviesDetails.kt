package academy.android.mymovie.ui

import academy.android.mymovie.MyMovieApp
import academy.android.mymovie.R
import academy.android.mymovie.adapters.ActorAdapter
import academy.android.mymovie.api.RetrofitInstance
import academy.android.mymovie.clickinterfaces.ActorClickInterface
import academy.android.mymovie.clickinterfaces.MovieClickInterface
import academy.android.mymovie.data.Repository
import academy.android.mymovie.databinding.FragmentMoviesDetailsBinding
import academy.android.mymovie.decorators.ActorItemDecoration
import academy.android.mymovie.models.Actor
import academy.android.mymovie.models.Movie
import academy.android.mymovie.utils.Constants.DEFAULT_IMAGE_URL
import academy.android.mymovie.utils.Constants.DEFAULT_SIZE
import academy.android.mymovie.utils.Constants.MOVIE_KEY
import academy.android.mymovie.viewmodelfactories.DetailsViewModelFactory
import academy.android.mymovie.viewmodels.DetailsViewModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class FragmentMoviesDetails : Fragment() {
    private var _binding: FragmentMoviesDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var adapter: ActorAdapter
    private var movieClickInterface: MovieClickInterface? = null
    private var actorClickInterface: ActorClickInterface? = null
    private var profileUrl: String? = null
    private var backdropUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        detailsViewModel = ViewModelProvider(
            this, DetailsViewModelFactory(
                Repository(RetrofitInstance.movieApi),
                arguments?.getInt(MOVIE_KEY)
                    ?: throw NullPointerException("No id for current movie"),
                (requireActivity().application as MyMovieApp).configurationService
            )
        ).get(DetailsViewModel::class.java)

        detailsViewModel.backdropUrl.observe(viewLifecycleOwner) {
            backdropUrl = it
        }
        detailsViewModel.profileUrl.observe(viewLifecycleOwner) {
            profileUrl = it
        }

        adapter = ActorAdapter(actorClickInterface!!, profileUrl ?: DEFAULT_IMAGE_URL + DEFAULT_SIZE)

        setupViews()
    }

    override fun onStart() {
        super.onStart()

        detailsViewModel.currentMovie.observe(this, this::updateView)
        detailsViewModel.isLoading.observe(this, this::setLoading)
        detailsViewModel.isLoadingActors.observe(this, this::setLoadingActors)
        detailsViewModel.actorsList.observe(this, this::setActorsViews)
        detailsViewModel.errorMessage.observe(viewLifecycleOwner) {
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

        if (context is ActorClickInterface) {
            actorClickInterface = context
        } else {
            throw IllegalArgumentException("Activity is not ActorClickInterface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        movieClickInterface = null
        actorClickInterface = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateView(currentMovie: Movie) {

        binding.apply {
            txvGenres.text = currentMovie.getGenres()

            txvTitle.text = currentMovie.title
            txvOverview.text = currentMovie.overview
            if (currentMovie.getCertification().isNotEmpty()) {
                txvAge.text = currentMovie.getCertification()
            } else {
                txvAge.visibility = TextView.INVISIBLE
            }
            txvReviews.text =
                getString(R.string.reviews, currentMovie.voteCount)
            rbRating.rating =
                currentMovie.voteAverage / 2
            if (currentMovie.backdropPath != null) {
                Glide.with(requireActivity())
                    .load((backdropUrl ?: DEFAULT_IMAGE_URL + DEFAULT_SIZE) + currentMovie.backdropPath)
                    .apply(imageOption)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imvBackdrop)
            } else {
                imvBackdrop.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.sample_placeholder
                    )
                )
            }
        }
    }

    private fun setupViews() {
        binding.apply {
            rvActors.addItemDecoration(
                ActorItemDecoration(resources.getDimension(R.dimen.dp8).toInt())
            )

            rvActors.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            rvActors.adapter = adapter

            txvBack.setOnClickListener { movieClickInterface?.onBackClick() }
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
            .dontAnimate()
            .dontTransform()
            .placeholder(R.drawable.sample_placeholder)
            .fallback(R.drawable.sample_placeholder)
    }
}