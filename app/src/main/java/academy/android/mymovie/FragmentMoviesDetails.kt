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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.lang.StringBuilder

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
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(DetailsViewModel::class.java)

        detailsViewModel.selectItem(
            arguments?.getInt("movie_id") ?: throw NullPointerException("No id for current movie")
        )

        detailsViewModel.selectedItem.observe(this, this::updateView)
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

    //method for updating view with movie data
    private fun updateView(currentMovie: Movie) {
        //if data about movie doesn`t contain list of actors, don`t show 'Cast' text
        if (currentMovie.actors.isEmpty()) {
            rootView.findViewById<TextView>(R.id.txv_cast).visibility =
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
            .into(rootView.findViewById(R.id.main_image))

        //set data to other views
        rootView.findViewById<TextView>(R.id.txv_title).text = currentMovie.title
        rootView.findViewById<TextView>(R.id.txv_tagline).text = stringBuilder
        rootView.findViewById<TextView>(R.id.txv_about).text = currentMovie.overview
        rootView.findViewById<RatingBar>(R.id.rating_bar).rating = currentMovie.ratings / 2
        "${currentMovie.minimumAge}+".also {
            rootView.findViewById<TextView>(R.id.txv_age).text = it
        }
        "${currentMovie.numberOfRatings} reviews".also {
            rootView.findViewById<TextView>(R.id.txv_reviews).text = it
        }
    }

    //method for setting views on fragment startup
    private fun setupViews() {
        rootView.findViewById<TextView>(R.id.txv_back).apply {
            setOnClickListener {
                movieClickInterface?.onBackClick()
            }
        }

        val recyclerViewActors = rootView.findViewById<RecyclerView>(R.id.recycler_view_actors)
        recyclerViewActors.layoutManager =
            LinearLayoutManager(rootView.context, RecyclerView.HORIZONTAL, false)
        recyclerViewActors.adapter = adapter
    }

    companion object {
        val imageOption = RequestOptions()
            .placeholder(R.drawable.sample_placeholder)
            .fallback(R.drawable.sample_placeholder)
    }
}