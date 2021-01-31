package academy.android.mymovie.ui

import academy.android.mymovie.R
import academy.android.mymovie.clickinterface.ActorClickInterface
import academy.android.mymovie.clickinterface.ContainerListener
import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.utils.Constants.ACTOR_KEY
import academy.android.mymovie.utils.Constants.MOVIE_KEY
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), MovieClickInterface, ActorClickInterface,
    ContainerListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setStatusBarTransparent()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.container_frame_layout, FragmentContainer())
                commit()
            }
        }
    }

    override fun onMovieClick(id: Int) {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            addToBackStack(null)
            add(R.id.container_frame_layout, FragmentMoviesDetails().apply {
                val bundle = Bundle()
                bundle.putInt(MOVIE_KEY, id)
                arguments = bundle
            })
            commit()
        }
    }

    override fun onActorClick(id: Int) {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            addToBackStack(null)
            add(R.id.container_frame_layout, FragmentActor().apply {
                val bundle = Bundle()
                bundle.putInt(ACTOR_KEY, id)
                arguments = bundle
            })
            commit()
        }
    }

    override fun onBackClick() {
        supportFragmentManager.popBackStack()
    }

    override fun addSearchFragment(searchText: String) {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.main_container, FragmentSearch().apply {
                val bundle = Bundle()
                bundle.putString("searchText", searchText)
                arguments = bundle
            })
            addToBackStack(null)
            commit()
        }
    }

    private fun setStatusBarTransparent() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        )

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.BLACK
    }
}
