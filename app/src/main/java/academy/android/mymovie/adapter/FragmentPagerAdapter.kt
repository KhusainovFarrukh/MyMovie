package academy.android.mymovie.adapter

import academy.android.mymovie.ui.FragmentMoviesList
import academy.android.mymovie.utils.Constants.KEY_NOWPLAYING
import academy.android.mymovie.utils.Constants.KEY_POPULAR
import academy.android.mymovie.utils.Constants.KEY_TOPRATED
import academy.android.mymovie.utils.Constants.KEY_UPCOMING
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentMoviesList().newInstance(KEY_POPULAR)
            1 -> FragmentMoviesList().newInstance(KEY_TOPRATED)
            2 -> FragmentMoviesList().newInstance(KEY_NOWPLAYING)
            3 -> FragmentMoviesList().newInstance(KEY_UPCOMING)
            else -> throw IndexOutOfBoundsException("There are only 4 fragments")
        }
    }

}