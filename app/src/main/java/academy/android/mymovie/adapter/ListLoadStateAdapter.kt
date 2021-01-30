package academy.android.mymovie.adapter

import academy.android.mymovie.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 *Created by FarrukhKhusainov on 1/30/21 1:55 PM
 **/
class ListLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ListLoadStateAdapter.ListLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ListLoadStateViewHolder {
        return ListLoadStateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_list_load_state, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListLoadStateViewHolder, loadState: LoadState) {
        holder.onBindLoadState(loadState)
    }

    inner class ListLoadStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val prbLoading = itemView.findViewById<ProgressBar>(R.id.prb_loading)
        private val txvError = itemView.findViewById<TextView>(R.id.txv_error)
        private val btnRetry = itemView.findViewById<Button>(R.id.btn_retry)

        init {
            btnRetry.setOnClickListener { retry.invoke() }
        }

        fun onBindLoadState(loadState: LoadState) {
            prbLoading.isVisible = loadState is LoadState.Loading
            txvError.isVisible = loadState !is LoadState.Loading
            btnRetry.isVisible = loadState !is LoadState.Loading
        }

    }
}