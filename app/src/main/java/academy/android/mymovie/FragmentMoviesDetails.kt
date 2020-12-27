package academy.android.mymovie

import academy.android.mymovie.adapter.ActorAdapter
import academy.android.mymovie.adapter.MovieClickInterface
import academy.android.mymovie.data.Movie
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
import java.lang.Exception
import java.lang.StringBuilder

class FragmentMoviesDetails : Fragment() {
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
        recyclerViewActors.layoutManager =
            LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
        recyclerViewActors.adapter = adapter


        val currentMovie: Movie = arguments?.getParcelable<Movie>("movie")
            ?: throw NullPointerException("There is np movie daat")

        //if data about movie doesn`t contain list of actors, don`t show 'Cast' text
        if (currentMovie.actors.isEmpty()) {
            view.findViewById<TextView>(R.id.txv_cast).visibility =
                View.INVISIBLE
        } else {
            adapter.submitList(currentMovie.actors)
        }

        //get genres by id and set to textview
        val stringBuilder = StringBuilder()
        currentMovie.genres.forEach {
            stringBuilder.append(it.name)
            if (it != currentMovie.genres.last()) {
                stringBuilder.append(", ")
            }
        }

        //load backdrop image
        Glide.with(requireActivity())
            .load(currentMovie.backdrop)
            .apply(imageOption)
            .into(view.findViewById(R.id.main_image))

        //set data to other views
        view.findViewById<TextView>(R.id.txv_title).text = currentMovie.title
        view.findViewById<TextView>(R.id.txv_tagline).text = stringBuilder
        view.findViewById<TextView>(R.id.txv_about).text = currentMovie.overview
        view.findViewById<RatingBar>(R.id.rating_bar).rating = currentMovie.ratings / 2
        "${currentMovie.minimumAge}+".also { view.findViewById<TextView>(R.id.txv_age).text = it }
        "${currentMovie.numberOfRatings} reviews".also {
            view.findViewById<TextView>(R.id.txv_reviews).text = it
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieClickInterface)
            movieClickInterface = context
    }

    override fun onDetach() {
        super.onDetach()
        movieClickInterface = null
    }
}