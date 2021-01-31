package academy.android.mymovie.adapter

import academy.android.mymovie.R
import academy.android.mymovie.callback.MovieCallback
import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.model.Movie
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
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class MovieAdapter(
    private val movieClickInterface: MovieClickInterface,
    private val imageUrl: String
) : PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieCallback()) {

    //variables to know if there is header/footer in the list or not
    private var hasHeader = false
    private var hasFooter = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        ViewHolderMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if (getItem(position) != null) {
            holder.onBindMovie(getItem(position) ?: throw NullPointerException("WTF"))
        }
    }

    override fun getItemViewType(position: Int): Int {

        //if there is header in the list...
        return if (hasHeader) {
            //...and also there is footer, position of the footer will be 'itemCount + 1'

            /*
            Proof to it:

            if we change 'itemCount + 1' to 'itemCount' only and do that:

            scroll list to very bottom and when footer is appeared
            fast scroll to top of the list to have header, and fast scroll bottom again to see footer,
            footer will be with spanSize = 1 (half of screen width) and
            last movie item with spanSize = 2 (full screen width)
            (added screenshots to Pull Request comment)

            It can be seen clearly when there is low network speed and maxSize is set to 60
            (in Repository.kt 25 and 34 line)
            */
            if (hasFooter && position == itemCount + 1) {
                VIEW_TYPE_LOADING

                //...and if position is 0, it will be header
            } else if (position == 0) {
                VIEW_TYPE_LOADING

                //...else it is movie item
            } else {
                VIEW_TYPE_MOVIE
            }

            //if there isn't header in the list
        } else {

            //...but there is footer, position of footer will be itemCount
            if (hasFooter && position == itemCount) {
                VIEW_TYPE_LOADING

                //...else it is movie item
            } else {
                VIEW_TYPE_MOVIE
            }
        }
    }

    //function for setting custom LoadStateAdapters, because default one does not show loadState on initial request
    fun withCustomLoadStateHeaderAndFooter(
        header: LoadStateAdapter<*>,
        footer: LoadStateAdapter<*>,
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->

            //if append or refresh state is Loading or Showing error text, there is footer in the list
            hasFooter = loadStates.append is LoadState.Loading ||
                    loadStates.append is LoadState.Error ||
                    loadStates.refresh is LoadState.Loading ||
                    loadStates.refresh is LoadState.Error

            //if prepend state is Loading or Showing error text, there is header in the list
            hasHeader = loadStates.prepend is LoadState.Loading ||
                    loadStates.prepend is LoadState.Error

            header.loadState = loadStates.prepend
            footer.loadState = when (loadStates.refresh) {
                is LoadState.NotLoading -> loadStates.append
                else -> loadStates.refresh
            }
        }
        return ConcatAdapter(header, this, footer)
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

                /*
                placeholder() replaced with thumbnail()
                because: after adding crossFade() and placeholder, centerCrop not working on
                first image load
                issue and possible solution: https://github.com/bumptech/glide/issues/1456#issuecomment-245540330
                */

                Glide.with(itemView.context)
                    .load(imageUrl + movie.posterPath)
                    .thumbnail(
                        Glide.with(itemView.context)
                            .load(R.drawable.sample_placeholder)
                            .centerCrop()
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .error(R.drawable.sample_placeholder)
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
        const val VIEW_TYPE_MOVIE = 1
        const val VIEW_TYPE_LOADING = 2
    }
}


