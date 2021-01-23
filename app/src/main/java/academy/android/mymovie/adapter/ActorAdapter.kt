package academy.android.mymovie.adapter

import academy.android.mymovie.R
import academy.android.mymovie.callback.ActorCallback
import academy.android.mymovie.data.Actor
import academy.android.mymovie.utils.Constants.IMAGE_URL
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ActorAdapter : ListAdapter<Actor, ActorViewHolder>(ActorCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder =
        ActorViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_actor, parent, false)
        )

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.onBindActor(getItem(position))
    }
}

class ActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val txvActor: TextView = itemView.findViewById(R.id.txv_actor)
    private val imvActor: ImageView = itemView.findViewById(R.id.imv_actor)

    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.sample_placeholder)
            .fallback(R.drawable.sample_placeholder)
    }

    fun onBindActor(actor: Actor) {
        if (actor.profileUrl == null) {
            imvActor.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.sample_placeholder
                )
            )
        } else {
            Glide.with(itemView.context)
                .load(Uri.parse(IMAGE_URL + actor.profileUrl))
                .apply(imageOption)
                .into(imvActor)
        }

        txvActor.text = actor.name
    }
}