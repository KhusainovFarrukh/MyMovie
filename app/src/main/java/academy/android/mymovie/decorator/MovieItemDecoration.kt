package academy.android.mymovie.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class MovieItemDecoration(val marginHorizontal: Int, val marginTop: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            //if item is not in the first row, add margin to top
            if ((parent.getChildAdapterPosition(view) != 0) && (parent.getChildAdapterPosition(view) != 1)) {
                top = marginTop
            }

            left = marginHorizontal
            right = marginHorizontal
        }
    }
}