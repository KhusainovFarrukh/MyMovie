package academy.android.mymovie

import academy.android.mymovie.adapter.ActorAdapter
import academy.android.mymovie.adapter.MovieClickInterface
import academy.android.mymovie.model.Movie
import android.content.Context
import android.graphics.Rect
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

class FragmentMoviesDetails : Fragment() {
    private val adapter = ActorAdapter()
    private var currentMovie: Movie? = null
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

        currentMovie = arguments?.getSerializable("movie") as Movie?

        //if data about movie doesn`t contain list of actors, don`t show 'Cast' text
        if (currentMovie?.actors == null) view.findViewById<TextView>(R.id.txv_cast).visibility =
            View.INVISIBLE

        view.findViewById<TextView>(R.id.txv_title).text = currentMovie?.name
        view.findViewById<TextView>(R.id.txv_tagline).text = currentMovie?.tagline
        view.findViewById<TextView>(R.id.txv_about).text = currentMovie?.storyline
        view.findViewById<TextView>(R.id.txv_age).text = "${currentMovie?.age}+"
        view.findViewById<TextView>(R.id.txv_reviews).text = "${currentMovie?.reviews} reviews"
        view.findViewById<RatingBar>(R.id.rating_bar).rating = currentMovie?.rating!!
        Glide.with(requireActivity())
            .load(currentMovie?.bannerUrl)
            .apply(imageOption)
            .into(view.findViewById(R.id.main_image))
    }

    override fun onStart() {
        super.onStart()
        adapter.submitList(currentMovie?.actors)
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

private class ActorItemDecoration(val horizontalMargin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            //if item is not first item, add margin to start
            if (parent.getChildAdapterPosition(view) != 0) left = horizontalMargin

        }
    }
}