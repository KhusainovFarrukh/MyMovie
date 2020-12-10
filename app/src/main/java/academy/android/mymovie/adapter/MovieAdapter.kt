package academy.android.mymovie.adapter

import academy.android.mymovie.R
import academy.android.mymovie.callback.MovieCallback
import academy.android.mymovie.model.Movie
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MovieAdapter : ListAdapter<Movie, MovieViewHolder>(MovieCallback()) {

    var movieClickInterface: MovieClickInterface? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_movie, parent, false)
        )


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.onBindMovie(getItem(position))
        holder.itemView.apply {
            setOnClickListener {
                movieClickInterface?.onMovieClick(position)
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
    private val imvFavorite: ImageView = itemView.findViewById(R.id.imv_favorite)

    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.sample_placeholder)
            .fallback(R.drawable.sample_placeholder)
    }

    fun onBindMovie(movie: Movie) {
        Glide.with(itemView.context)
            .load(Uri.parse(movie.imageUrl))
            .apply(imageOption)
            .into(imvImage)
        txvName.text = movie.name
        txvTime.text = "${movie.time} min"
        txvReviews.text = "${movie.reviews} reviews"
        txvTagline.text = movie.tagline
        txvAge.text = "${movie.age}+"
        rbRating.rating = movie.rating
        imvFavorite.setImageDrawable(
            if (movie.isFavorite) ResourcesCompat.getDrawable(
                itemView.context.resources,
                R.drawable.ic_like,
                null
            )
            else ResourcesCompat.getDrawable(
                itemView.context.resources,
                R.drawable.ic_like_empty,
                null
            )
        )
    }

}

interface MovieClickInterface {
    fun onMovieClick(position: Int)
}
