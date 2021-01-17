package academy.android.mymovie.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class ActorItemDecoration(val horizontalMargin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            //if item is not first item, add margin to start
            if (parent.getChildAdapterPosition(view) != 0) left = horizontalMargin

        }
    }
}