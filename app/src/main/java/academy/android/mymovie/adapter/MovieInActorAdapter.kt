package academy.android.mymovie.adapter

import academy.android.mymovie.R
import academy.android.mymovie.callback.MovieInActorCallabck
import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.data.MovieInActor
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MovieInActorAdapter(
    private val movieClickInterface: MovieClickInterface,
    private val imageUrl: String
) :
    ListAdapter<MovieInActor, MovieInActorAdapter.MovieViewHolder>(MovieInActorCallabck()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_filmography, parent, false)
        )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.onBindActor(getItem(position))
        holder.itemView.apply {
            setOnClickListener { movieClickInterface.onMovieClick(getItem(position).id) }
        }
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txvName: TextView = itemView.findViewById(R.id.txv_name)
        private val imvImage: ImageView = itemView.findViewById(R.id.imv_image)

        fun onBindActor(movie: MovieInActor) {
            if (movie.imageUrl == null) {
                imvImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.sample_placeholder
                    )
                )
            } else {
                Glide.with(itemView.context)
                    .load(Uri.parse(imageUrl + movie.imageUrl))
                    .apply(imageOption)
                    .into(imvImage)
            }
            txvName.text = movie.title
        }
    }

    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.sample_placeholder)
            .fallback(R.drawable.sample_placeholder)
    }
}