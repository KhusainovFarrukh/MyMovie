package academy.android.mymovie.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class MovieItemDecoration(private val marginHorizontal: Int, private val marginVertical: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            /*
            Changed adding top margin to adding top an bottom margin

            because: VIEW_TYPE_LOADING has been added to Movie Adapter, so we can`t add top margin
            by checking if item is in the 0th position or 1st position
            For example: if there is header in the recycler view, on the 0th position will be header and
             we mustn't add marginTop to items on positions 1st and 2nd.
            if there isn`t header in the recycler view, on the 0th position will be movie item and
            we we mustn't add marginTop to items on positions 0th and 1st

            So, there is change: marginTop replaced with marginVertical. In other words,
            vertical margin divided to 2parts: top and bottom

            top = 8dp and bottom = 8dp
             */

            top = marginVertical
            bottom = marginVertical
            left = marginHorizontal
            right = marginHorizontal
        }
    }
}