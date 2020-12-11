package academy.android.mymovie

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class FragmentMoviesList : Fragment() {

    private var movieClickListener: MovieClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = inflater.inflate(R.layout.fragment_movies_list, container, false)

        contentView.findViewById<CardView>(R.id.cardview_item).apply {
            setOnClickListener {
                movieClickListener?.onMovieClick()
            }
        }
        return contentView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        movieClickListener = context as MovieClickListener
    }

    override fun onDetach() {
        super.onDetach()
        movieClickListener = null
    }
}

interface MovieClickListener {
    fun onMovieClick()
}