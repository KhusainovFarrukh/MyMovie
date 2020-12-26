package academy.android.mymovie

import academy.android.mymovie.adapter.MovieAdapter
import academy.android.mymovie.adapter.MovieClickInterface
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentMoviesList : Fragment() {

    private val adapter = MovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.addItemDecoration(
            MovieItemDecoration(
                resources.getDimension(R.dimen.dp16).toInt(),
                resources.getDimension(R.dimen.dp18).toInt()
            )
        )
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.submitList(MoviesDatabase().getMoviesList())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieClickInterface)
            adapter.movieClickInterface = context
    }

    override fun onDetach() {
        super.onDetach()
        adapter.movieClickInterface = null
    }
}

private class MovieItemDecoration(val marginHorizontal: Int, val marginTop: Int) :
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
            //if item is on the left side, add margin to end
            if (parent.getChildAdapterPosition(view) % 2 == 0) {
                right = marginHorizontal
            }
        }
    }
}