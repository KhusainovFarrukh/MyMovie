package academy.android.mymovie.adapter

import academy.android.mymovie.R
import academy.android.mymovie.callback.MovieInActorCallabck
import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.data.MovieInActor
import academy.android.mymovie.databinding.ViewHolderFilmographyBinding
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MovieInActorAdapter(
    private val movieClickInterface: MovieClickInterface,
    private val imageUrl: String
) : ListAdapter<MovieInActor, MovieInActorAdapter.MovieViewHolder>(MovieInActorCallabck()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        ViewHolderFilmographyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.onBindActor(getItem(position))
    }

    inner class MovieViewHolder(private val binding: ViewHolderFilmographyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                movieClickInterface.onMovieClick(getItem(adapterPosition).id)
            }
        }

        fun onBindActor(movie: MovieInActor) {
            binding.apply {
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
    }

    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.sample_placeholder)
            .fallback(R.drawable.sample_placeholder)
    }
}