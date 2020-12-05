package academy.android.mymovie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FragmentMoviesDetails : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = inflater.inflate(R.layout.fragment_movies_details, container, false)

        contentView.findViewById<TextView>(R.id.txv_back).apply {
            setOnClickListener{
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        return contentView
    }

}