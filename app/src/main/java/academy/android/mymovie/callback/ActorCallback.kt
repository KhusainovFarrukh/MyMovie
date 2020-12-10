package academy.android.mymovie.callback

import academy.android.mymovie.model.Actor
import androidx.recyclerview.widget.DiffUtil

class ActorCallback : DiffUtil.ItemCallback<Actor>() {
    override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem == newItem
    }
}