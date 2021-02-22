package academy.android.mymovie.adapter

import academy.android.mymovie.R
import academy.android.mymovie.callback.ActorCallback
import academy.android.mymovie.clickinterface.ActorClickInterface
import academy.android.mymovie.databinding.ViewHolderActorBinding
import academy.android.mymovie.model.Actor
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


class ActorAdapter(
    private val actorClickInterface: ActorClickInterface,
    private val imageUrl: String
) : ListAdapter<Actor, ActorAdapter.ActorViewHolder>(ActorCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder =
        ActorViewHolder(
            ViewHolderActorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.onBindActor(getItem(position))
    }

    inner class ActorViewHolder(private val binding: ViewHolderActorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                actorClickInterface.onActorClick(getItem(absoluteAdapterPosition).id)
            }
        }

        fun onBindActor(actor: Actor) {
            if (actor.profileUrl != null && actor.profileUrl.isNotEmpty()) {
                //some changes. see comment on MovieAdapter.kt onBindMovie()
                binding.imvActor.setImageDrawable(null)
                Glide.with(itemView.context)
                    .load(Uri.parse(imageUrl + actor.profileUrl))
                    .thumbnail(
                        Glide.with(itemView.context)
                            .load(R.drawable.sample_placeholder)
                            .centerCrop()
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .error(R.drawable.sample_placeholder)
                    .into(binding.imvActor)
            } else {
                Glide.with(itemView.context).clear(binding.imvActor)
                binding.imvActor.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.sample_placeholder
                    )
                )
            }
            binding.txvActor.text = actor.name
        }
    }
}