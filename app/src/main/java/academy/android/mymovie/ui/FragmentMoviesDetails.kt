package academy.android.mymovie.ui

import academy.android.mymovie.R
import academy.android.mymovie.adapter.ActorAdapter
import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.data.Movie
import academy.android.mymovie.decorator.ActorItemDecoration
import academy.android.mymovie.ui.MainActivity.Companion.MOVIE_KEY
import academy.android.mymovie.utils.Constants.IMAGE_URL
import academy.android.mymovie.viewmodel.DetailsViewModel
import academy.android.mymovie.viewmodelfactory.DetailsViewModelFactory
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FragmentMoviesDetails : Fragment() {
    private lateinit var rootView: View
    private lateinit var detailsViewModel: DetailsViewModel
    private val adapter = ActorAdapter()
    private var movieClickInterface: MovieClickInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rootView = view
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

    private fun updateView(currentMovie: Movie) {
        //if data about movie doesn`t contain list of actors, don`t show 'Cast' text
//        if (currentMovie.actors.isEmpty()) {
//            rootView.findViewById<TextView>(R.id.txv_cast).visibility =
//                View.INVISIBLE
//        } else {
//            rootView.findViewById<TextView>(R.id.txv_cast).visibility =
//                View.VISIBLE
//            adapter.submitList(currentMovie.actors)
//        }

        currentMovie.genres.forEach {
            rootView.findViewById<TextView>(R.id.txv_tagline).append(it.name)
            if (it != currentMovie.genres.last()) {
                rootView.findViewById<TextView>(R.id.txv_tagline).append(", ")
            }
        }

        rootView.findViewById<TextView>(R.id.txv_title).text = currentMovie.title
        rootView.findViewById<TextView>(R.id.txv_about).text = currentMovie.overview
        rootView.findViewById<TextView>(R.id.txv_age).text =
            getString(R.string.age, if (currentMovie.adult) 16 else 13)
        rootView.findViewById<TextView>(R.id.txv_reviews).text =
            getString(R.string.reviews, currentMovie.voteCount)
        rootView.findViewById<RatingBar>(R.id.rating_bar).rating =
            currentMovie.voteAverage / 2
        Glide.with(requireActivity())
            .load(IMAGE_URL + currentMovie.backdropPath)
            .apply(imageOption)
            .into(rootView.findViewById(R.id.main_image))
    }

    private fun setupViews() {
        val recyclerViewActors = rootView.findViewById<RecyclerView>(R.id.recycler_view_actors)
        recyclerViewActors.addItemDecoration(
            ActorItemDecoration(
                resources.getDimension(R.dimen.dp8)
                    .toInt()
            )
        )

        recyclerViewActors.layoutManager =
            LinearLayoutManager(rootView.context, RecyclerView.HORIZONTAL, false)
        recyclerViewActors.adapter = adapter

        rootView.findViewById<TextView>(R.id.txv_back).apply {
            setOnClickListener {
                movieClickInterface?.onBackClick()
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        rootView.findViewById<RelativeLayout>(R.id.progress_layout).isVisible = isLoading
    }

    private companion object {
        val imageOption = RequestOptions()
            .placeholder(R.drawable.sample_placeholder)
            .fallback(R.drawable.sample_placeholder)
    }
}