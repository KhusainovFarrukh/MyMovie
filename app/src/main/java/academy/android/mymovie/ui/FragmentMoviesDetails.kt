package academy.android.mymovie.ui

import academy.android.mymovie.ui.MainActivity.Companion.MOVIE_KEY
import academy.android.mymovie.MovieClickInterface
import academy.android.mymovie.MoviesDatabase
import academy.android.mymovie.R
import academy.android.mymovie.adapter.ActorAdapter
import academy.android.mymovie.decorator.ActorItemDecoration
import academy.android.mymovie.model.Movie
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
import java.lang.IllegalArgumentException

class FragmentMoviesDetails : Fragment() {
    private val adapter = ActorAdapter()
    private lateinit var currentMovie: Movie
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
        MoviesDatabase().getMoviesList().forEach {
            if (movieId == it.id) {
                currentMovie = it
                return@forEach
            }
        }

        //if data about movie doesn`t contain list of actors, don`t show 'Cast' text
        if (currentMovie.actors == null) view.findViewById<TextView>(R.id.txv_cast).visibility =
            View.INVISIBLE

        view.findViewById<TextView>(R.id.txv_title).text = currentMovie.name
        view.findViewById<TextView>(R.id.txv_tagline).text = currentMovie.tagline
        view.findViewById<TextView>(R.id.txv_about).text = currentMovie.storyline
        view.findViewById<TextView>(R.id.txv_age).text = getString(R.string.age, currentMovie.age)
        view.findViewById<TextView>(R.id.txv_reviews).text =
            getString(R.string.reviews, currentMovie.reviews)
        view.findViewById<RatingBar>(R.id.rating_bar).rating = currentMovie.rating
        Glide.with(requireActivity())
            .load(currentMovie.bannerUrl)
            .apply(imageOption)
            .into(view.findViewById(R.id.main_image))
    }

    override fun onStart() {
        super.onStart()
        adapter.submitList(currentMovie.actors)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieClickInterface) {
            movieClickInterface = context
        } else {
            throw IllegalArgumentException("Not MovieClickInterface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        movieClickInterface = null
    }
}