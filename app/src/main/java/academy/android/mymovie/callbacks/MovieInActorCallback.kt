package academy.android.mymovie.callbacks

import academy.android.mymovie.models.MovieInActor
import androidx.recyclerview.widget.DiffUtil

class MovieInActorCallback : DiffUtil.ItemCallback<MovieInActor>() {
    override fun areItemsTheSame(oldItem: MovieInActor, newItem: MovieInActor): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieInActor, newItem: MovieInActor): Boolean {
        return oldItem == newItem
    }
}