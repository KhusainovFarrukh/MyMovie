package academy.android.mymovie.ui

import academy.android.mymovie.R
import academy.android.mymovie.adapters.MovieInActorAdapter
import academy.android.mymovie.data.Repository
import academy.android.mymovie.api.RetrofitInstance
import academy.android.mymovie.clickinterfaces.ActorClickInterface
import academy.android.mymovie.clickinterfaces.MovieClickInterface
import academy.android.mymovie.models.ActorResponse
import academy.android.mymovie.models.MovieInActor
import academy.android.mymovie.databinding.FragmentActorBinding
import academy.android.mymovie.decorators.ActorItemDecoration
import academy.android.mymovie.utils.Constants
import academy.android.mymovie.utils.Constants.ACTOR_KEY
import academy.android.mymovie.utils.Constants.DEFAULT_IMAGE_URL
import academy.android.mymovie.utils.Constants.KEY_BACKDROP
import academy.android.mymovie.utils.Constants.KEY_BASE_URL
import academy.android.mymovie.utils.Constants.KEY_SHARED_PREF
import academy.android.mymovie.viewmodels.ActorViewModel
import academy.android.mymovie.viewmodelfactorys.ActorViewModelFactory
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
import com.bumptech.glide.request.RequestOptions
import kotlinx.serialization.ExperimentalSerializationApi
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalSerializationApi
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
                Repository(RetrofitInstance.movieApi),
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
            throw IllegalArgumentException("$context is not ActorClickInterface")
        }
        if (context is MovieClickInterface) {
            movieClickInterface = context
        } else {
            throw IllegalArgumentException("$context is not MovieClickInterface")
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

        binding.apply {
            txvName.text = currentActor.name
            txvBornPlace.text = currentActor.birthPlace
            txvJobs.text = currentActor.knownFor
            if (currentActor.biography.isNotEmpty()) {
                txvBiographyText.text = currentActor.biography
            } else {
                txvBiography.visibility = TextView.INVISIBLE
            }
            if (currentActor.birthday != null) {
                txvBornDate.text = formatDate(currentActor.birthday)
            }
            if (currentActor.imageUrl != null) {
                Glide.with(requireActivity())
                    .load(backdropUrl + currentActor.imageUrl)
                    .apply(imageOption)
                    .into(imvPerson)
            } else {
                imvPerson.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.sample_placeholder
                    )
                )
            }
            if (currentActor.images.profiles.isNotEmpty()) {
                Glide.with(requireActivity())
                    .load(backdropUrl + currentActor.images.profiles.random().imageUrl)
                    .apply(imageOption)
                    .into(imvBackdrop)
            } else {
                imvBackdrop.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.sample_placeholder
                    )
                )
            }
            setFilmography(currentActor.filmography.cast)
        }
    }

    private fun setupViews() {
        binding.apply {
            rvFilmography.addItemDecoration(
                ActorItemDecoration(resources.getDimension(R.dimen.dp8).toInt())
            )

            rvFilmography.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            rvFilmography.adapter = adapter

            txvBack.setOnClickListener { actorClickInterface?.onBackClick() }

        }
    }

    private fun setFilmography(movies: List<MovieInActor>) {
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

    private fun formatDate(inputDate: String): String {
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        val outputDate: Date
        var newDateString = ""
        try {
            outputDate = inputFormat.parse(inputDate) ?: throw Exception("Cannot parse date")
            newDateString =
                DateFormat.getDateInstance(DateFormat.LONG, Locale.US).format(outputDate)
        } catch (e: Exception) {
            Toast.makeText(
                requireActivity(),
                e.message,
                Toast.LENGTH_SHORT
            ).show()
        }

        return newDateString
    }

    private companion object {
        val imageOption = RequestOptions()
            .placeholder(R.drawable.sample_placeholder)
            .fallback(R.drawable.sample_placeholder)
    }
}