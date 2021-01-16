package academy.android.mymovie.ui

import academy.android.mymovie.MovieClickInterface
import academy.android.mymovie.MoviesDatabase
import academy.android.mymovie.R
import academy.android.mymovie.adapter.MovieAdapter
import academy.android.mymovie.decorator.MovieItemDecoration
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalArgumentException

class FragmentMoviesList : Fragment() {

    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.addItemDecoration(
            MovieItemDecoration(
                resources.getDimension(R.dimen.dp8).toInt(),
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
        if (context is MovieClickInterface) {
            adapter = MovieAdapter(context)
        } else {
            throw IllegalArgumentException("$context is not MovieClickInterface")
        }
    }
}