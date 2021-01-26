package academy.android.mymovie.ui

import academy.android.mymovie.R
import academy.android.mymovie.adapter.MovieInActorAdapter
import academy.android.mymovie.clickinterface.ActorClickInterface
import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.data.ActorResponse
import academy.android.mymovie.data.MovieInActor
import academy.android.mymovie.databinding.FragmentActorBinding
import academy.android.mymovie.decorator.ActorItemDecoration
import academy.android.mymovie.utils.Constants
import academy.android.mymovie.utils.Constants.ACTOR_KEY
import academy.android.mymovie.utils.Constants.DEFAULT_IMAGE_URL
import academy.android.mymovie.utils.Constants.KEY_BACKDROP
import academy.android.mymovie.utils.Constants.KEY_BASE_URL
import academy.android.mymovie.utils.Constants.KEY_SHARED_PREF
import academy.android.mymovie.viewmodel.ActorViewModel
import academy.android.mymovie.viewmodelfactory.ActorViewModelFactory
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

class FragmentActor : Fragment() {
    private var _binding: FragmentActorBinding? = null
    private val binding get() = _binding!!
    private lateinit var actorViewModel: ActorViewModel
    private lateinit var adapter: MovieInActorAdapter
    private lateinit var backdropUrl: String
    private var actorClickInterface: ActorClickInterface? = null
    private var movieClickInterface: MovieClickInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPrefs =
            requireActivity().getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE)

        backdropUrl = sharedPrefs.getString(KEY_BASE_URL, DEFAULT_IMAGE_URL) +
                sharedPrefs.getString(KEY_BACKDROP, Constants.DEFAULT_SIZE)

        adapter = MovieInActorAdapter(movieClickInterface!!, backdropUrl)

        setupViews()
    }

    override fun onStart() {
        super.onStart()

        actorViewModel = ViewModelProvider(
            this, ActorViewModelFactory(
                arguments?.getInt(ACTOR_KEY)
                    ?: throw NullPointerException("No id for current movie")
            )
        ).get(ActorViewModel::class.java)

        actorViewModel.currentActor.observe(this, this::updateView)
        actorViewModel.isLoading.observe(this, this::setLoading)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActorClickInterface) {
            actorClickInterface = context
        } else {
            throw IllegalArgumentException("Activity is not ActorClickInterface")
        }
        if (context is MovieClickInterface) {
            movieClickInterface = context
        } else {
            throw IllegalArgumentException("Activity is not MovieClickInterface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        actorClickInterface = null
        movieClickInterface = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateView(currentActor: ActorResponse) {

        binding.txvName.text = currentActor.name
        binding.txvBiographyText.text = currentActor.biography
        binding.txvBornDate.text = currentActor.birthday
        binding.txvBornPlace.text = currentActor.birthPlace

        currentActor.imageUrl?.let {
            Glide.with(requireActivity())
                .load(backdropUrl + it)
                .apply(imageOption)
                .into(binding.imvPerson)
        }

        if (currentActor.images.profiles.isNotEmpty()) {
            Glide.with(requireActivity())
                .load(backdropUrl + currentActor.images.profiles.random().imageUrl)
                .apply(imageOption)
                .into(binding.imvBackdrop)
        }

        setActorsViews(currentActor.filmography.cast)
    }

    private fun setupViews() {
        binding.rvFilmography.addItemDecoration(
            ActorItemDecoration(
                resources.getDimension(R.dimen.dp8)
                    .toInt()
            )
        )

        binding.rvFilmography.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvFilmography.adapter = adapter

        binding.txvBack.apply {
            setOnClickListener {
                actorClickInterface?.onBackClick()
            }
        }
    }

    private fun setActorsViews(movies: List<MovieInActor>) {
        if (movies.isEmpty()) {
            binding.txvFilmography.visibility =
                View.INVISIBLE
        } else {
            binding.txvFilmography.visibility =
                View.VISIBLE
            adapter.submitList(movies)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.viewLoading.isVisible = isLoading
    }

//    private fun setLoadingActors(isLoadingActors: Boolean) {
//        binding.prbLoadingActors.isVisible = isLoadingActors
//    }

    private companion object {
        val imageOption = RequestOptions()
            .placeholder(R.drawable.sample_placeholder)
            .fallback(R.drawable.sample_placeholder)
    }
}