package academy.android.mymovie.callback

import academy.android.mymovie.model.Movie
import androidx.recyclerview.widget.DiffUtil

class MovieCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}