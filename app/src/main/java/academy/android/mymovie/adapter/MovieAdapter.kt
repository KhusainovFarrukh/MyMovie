package academy.android.mymovie.adapter

import academy.android.mymovie.R
import academy.android.mymovie.callback.MovieCallback
import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.data.Movie
import academy.android.mymovie.databinding.ViewHolderMovieBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MovieAdapter(
    private val movieClickInterface: MovieClickInterface,
    private val imageUrl: String
) : PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        ViewHolderMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if (getItem(position) != null) {
            holder.onBindMovie(getItem(position) ?: throw NullPointerException("WTF"))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            2
        } else {
            1
        }
    }

    //function for setting custom footer, because default one not shows loadState on initial request
    fun withCustomFooter(
        footerAdapter: LoadStateAdapter<*>
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            footerAdapter.loadState = when (loadStates.refresh) {
                is LoadState.NotLoading -> loadStates.append
                else -> loadStates.refresh
            }
        }
        return ConcatAdapter(this, footerAdapter)
    }

    inner class MovieViewHolder(private val binding: ViewHolderMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                movieClickInterface.onMovieClick(
                    getItem(adapterPosition)?.id
                        ?: throw NullPointerException("There is no movie")
                )
            }
        }

        fun onBindMovie(movie: Movie) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(imageUrl + movie.posterPath)
                    .apply(imageOption)
                    .into(binding.imvImage)

                txvGenres.text = movie.getGenres()
                txvName.text = movie.title
                txvTime.text = itemView.context.getString(R.string.time, movie.runtime)
                if (movie.getCertification().isNotEmpty()) {
                    txvAge.text = movie.getCertification()
                    cvAgeHolder.visibility = TextView.VISIBLE
                } else {
                    cvAgeHolder.visibility = TextView.INVISIBLE
                }
                txvReviews.text = itemView.context.getString(R.string.reviews, movie.voteCount)
                rbRating.rating = movie.voteAverage / 2
            }

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

    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.sample_placeholder)
            .fallback(R.drawable.sample_placeholder)
    }
}


