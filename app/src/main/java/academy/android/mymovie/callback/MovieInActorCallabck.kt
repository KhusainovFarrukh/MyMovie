package academy.android.mymovie.callback

import academy.android.mymovie.data.MovieInActor
import androidx.recyclerview.widget.DiffUtil

class MovieInActorCallabck : DiffUtil.ItemCallback<MovieInActor>() {
    override fun areItemsTheSame(oldItem: MovieInActor, newItem: MovieInActor): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieInActor, newItem: MovieInActor): Boolean {
        return oldItem == newItem
    }
}