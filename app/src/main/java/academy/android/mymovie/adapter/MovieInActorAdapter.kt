package academy.android.mymovie.adapter

import academy.android.mymovie.R
import academy.android.mymovie.callback.MovieInActorCallback
import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.databinding.ViewHolderFilmographyBinding
import academy.android.mymovie.model.MovieInActor
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class MovieInActorAdapter(
    private val movieClickInterface: MovieClickInterface,
    private val imageUrl: String
) : ListAdapter<MovieInActor, MovieInActorAdapter.MovieViewHolder>(MovieInActorCallback()) {

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
                movieClickInterface.onMovieClick(getItem(bindingAdapterPosition).id)
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
                    //some changes. see comment on MovieAdapter.kt onBindMovie()
                    Glide.with(itemView.context)
                        .load(Uri.parse(imageUrl + movie.imageUrl))
                        .thumbnail(
                            Glide.with(itemView.context)
                                .load(R.drawable.sample_placeholder)
                                .centerCrop()
                        )
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .error(R.drawable.sample_placeholder)
                        .into(imvImage)
                }
                txvName.text = movie.title
            }
        }
    }
}