package academy.android.mymovie

import academy.android.mymovie.adapter.ActorAdapter
import academy.android.mymovie.model.Movie
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.txv_back).apply {
            setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        val imageOption = RequestOptions()
            .placeholder(R.drawable.sample_placeholder)
            .fallback(R.drawable.sample_placeholder)

        val recyclerViewActors = view.findViewById<RecyclerView>(R.id.recycler_view_actors)
        recyclerViewActors.layoutManager =
            LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
        recyclerViewActors.adapter = adapter

        currentMovie = arguments?.getSerializable("movie") as Movie?

        //if data about movie doesn`t contain list of actors, don`t show 'Cast' text
        if (currentMovie?.actors == null) view.findViewById<TextView>(R.id.txv_cast).visibility = View.INVISIBLE

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
}