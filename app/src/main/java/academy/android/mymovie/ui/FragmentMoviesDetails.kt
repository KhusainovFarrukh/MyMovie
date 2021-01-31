package academy.android.mymovie.ui

import academy.android.mymovie.R
import academy.android.mymovie.adapter.ActorAdapter
import academy.android.mymovie.api.Repository
import academy.android.mymovie.api.RetrofitInstance
import academy.android.mymovie.clickinterface.ActorClickInterface
import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.model.Actor
import academy.android.mymovie.model.Movie
import academy.android.mymovie.databinding.FragmentMoviesDetailsBinding
import academy.android.mymovie.decorator.ActorItemDecoration
import academy.android.mymovie.utils.Constants
import academy.android.mymovie.utils.Constants.DEFAULT_IMAGE_URL
import academy.android.mymovie.utils.Constants.KEY_BACKDROP
import academy.android.mymovie.utils.Constants.KEY_BASE_URL
import academy.android.mymovie.utils.Constants.KEY_PROFILE
import academy.android.mymovie.utils.Constants.KEY_SHARED_PREF
import academy.android.mymovie.utils.Constants.MOVIE_KEY
import academy.android.mymovie.viewmodel.DetailsViewModel
import academy.android.mymovie.viewmodelfactory.DetailsViewModelFactory
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class FragmentMoviesDetails : Fragment() {
    private var _binding: FragmentMoviesDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var adapter: ActorAdapter
    private lateinit var backdropUrl: String
    private var movieClickInterface: MovieClickInterface? = null
    private var actorClickInterface: ActorClickInterface? = null

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

        adapter = ActorAdapter(actorClickInterface!!, imageUrl)

        backdropUrl = sharedPrefs.getString(KEY_BASE_URL, DEFAULT_IMAGE_URL) +
                sharedPrefs.getString(KEY_BACKDROP, Constants.DEFAULT_SIZE)

        setupViews()
    }

    override fun onStart() {
        super.onStart()

        detailsViewModel = ViewModelProvider(
            this, DetailsViewModelFactory(
                Repository(RetrofitInstance.movieApi),
                arguments?.getInt(MOVIE_KEY)
                    ?: throw NullPointerException("No id for current movie")
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
                    .load(backdropUrl + currentMovie.backdropPath)
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