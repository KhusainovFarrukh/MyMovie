package academy.android.mymovie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), MovieClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.container_frame_layout, FragmentMoviesList())
                commit()
            }
        }
    }

    override fun onMovieClick() {
        supportFragmentManager.beginTransaction().apply {
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

    override fun onBackClick() {
        supportFragmentManager.popBackStack()
    }
}