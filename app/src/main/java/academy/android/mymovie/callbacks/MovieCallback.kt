package academy.android.mymovie.callbacks

import academy.android.mymovie.models.Movie
import androidx.recyclerview.widget.DiffUtil

class MovieCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}