package academy.android.mymovie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView

class FragmentMoviesList : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = inflater.inflate(R.layout.fragment_movies_list, container, false)

        contentView.findViewById<CardView>(R.id.cardview_item).apply {
            setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out
                    )
                    addToBackStack(null)
                    add(R.id.container_frame_layout, FragmentMoviesDetails())
                    commit()
                }
            }
        }

        return contentView
    }

}