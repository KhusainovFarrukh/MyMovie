package academy.android.mymovie.ui

import academy.android.mymovie.ui.MainActivity.Companion.MOVIE_KEY
import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.R
import academy.android.mymovie.adapter.ActorAdapter
import academy.android.mymovie.data.loadMovies
import academy.android.mymovie.decorator.ActorItemDecoration
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.IllegalArgumentException

class FragmentMoviesDetails : Fragment() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default + Job())
    private val adapter = ActorAdapter()
    private var movieClickInterface: MovieClickInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.txv_back).apply {
            setOnClickListener {
                movieClickInterface?.onBackClick()
            }
        }

        val imageOption = RequestOptions()
            .placeholder(R.drawable.sample_placeholder)
            .fallback(R.drawable.sample_placeholder)

        val recyclerViewActors = view.findViewById<RecyclerView>(R.id.recycler_view_actors)
        recyclerViewActors.addItemDecoration(
            ActorItemDecoration(
                resources.getDimension(R.dimen.dp8)
                    .toInt()
            )
        )

        recyclerViewActors.layoutManager =
            LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
        recyclerViewActors.adapter = adapter

        val movieId = arguments?.getString(MOVIE_KEY)
        coroutineScope.launch {
            loadMovies(context!!).forEach {
                if (movieId == it.id.toString()) {
                    val currentMovie = it

                    activity?.runOnUiThread {
                        //if data about movie doesn`t contain list of actors, don`t show 'Cast' text
                        if (currentMovie.actors.isEmpty()) {
                            view.findViewById<TextView>(R.id.txv_cast).visibility =
                                View.INVISIBLE
                        } else {
                            adapter.submitList(currentMovie.actors)
                        }

                        currentMovie.genres.forEach { currentGenre ->
                            view.findViewById<TextView>(R.id.txv_tagline).append(currentGenre.name)
                            if (currentGenre != currentMovie.genres.last()) {
                                view.findViewById<TextView>(R.id.txv_tagline).append(", ")
                            }
                        }

                        view.findViewById<TextView>(R.id.txv_title).text = currentMovie.title
                        view.findViewById<TextView>(R.id.txv_about).text = currentMovie.overview
                        view.findViewById<TextView>(R.id.txv_age).text =
                            getString(R.string.age, currentMovie.minimumAge)
                        view.findViewById<TextView>(R.id.txv_reviews).text =
                            getString(R.string.reviews, currentMovie.numberOfRatings)
                        view.findViewById<RatingBar>(R.id.rating_bar).rating =
                            currentMovie.ratings / 2
                        Glide.with(requireActivity())
                            .load(currentMovie.backdrop)
                            .apply(imageOption)
                            .into(view.findViewById(R.id.main_image))
                    }

                    return@forEach
                }
            }
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
}