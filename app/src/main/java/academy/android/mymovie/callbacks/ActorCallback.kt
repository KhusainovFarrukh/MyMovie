package academy.android.mymovie.callbacks

import academy.android.mymovie.models.Actor
import androidx.recyclerview.widget.DiffUtil

class ActorCallback : DiffUtil.ItemCallback<Actor>() {
    override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem == newItem
    }
}