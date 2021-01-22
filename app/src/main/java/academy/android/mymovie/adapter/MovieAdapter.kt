package academy.android.mymovie.adapter

import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.R
import academy.android.mymovie.callback.MovieCallback
import academy.android.mymovie.data.Movie
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MovieAdapter(private val movieClickInterface: MovieClickInterface) :
    ListAdapter<Movie, MovieViewHolder>(MovieCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_movie, parent, false)
        )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.onBindMovie(getItem(position))
        holder.itemView.apply {
            setOnClickListener {
                movieClickInterface.onMovieClick(getItem(position).id)
            }
        }
    }
}

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val txvName: TextView = itemView.findViewById(R.id.txv_name)
    private val txvTime: TextView = itemView.findViewById(R.id.txv_time)
    private val txvReviews: TextView = itemView.findViewById(R.id.txv_reviews)
    private val txvTagline: TextView = itemView.findViewById(R.id.txv_tagline)
    private val txvAge: TextView = itemView.findViewById(R.id.txv_age)
    private val rbRating: RatingBar = itemView.findViewById(R.id.rating_bar)
    private val imvImage: ImageView = itemView.findViewById(R.id.imv_image)

    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.sample_placeholder)
            .fallback(R.drawable.sample_placeholder)
    }

    fun onBindMovie(movie: Movie) {
        Glide.with(itemView.context)
            .load(Uri.parse(movie.poster))
            .apply(imageOption)
            .into(imvImage)

        movie.genres.forEach { currentGenre ->
            txvTagline.append(currentGenre.name)
            if (currentGenre != movie.genres.last()) {
                txvTagline.append(", ")
            }
        }

        txvName.text = movie.title
        txvTime.text = itemView.context.getString(R.string.time, movie.runtime)
        txvAge.text = itemView.context.getString(R.string.age, movie.minimumAge)
        txvReviews.text = itemView.context.getString(R.string.reviews, movie.numberOfRatings)
        rbRating.rating = movie.ratings / 2
//        imvFavorite.setImageDrawable(
//            if (movie.isFavorite) ResourcesCompat.getDrawable(
//                itemView.context.resources,
//                R.drawable.ic_like,
//                null
//            )
//            else ResourcesCompat.getDrawable(
//                itemView.context.resources,
//                R.drawable.ic_like_empty,
//                null
//            )
//        )
    }

}
