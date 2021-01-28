package academy.android.mymovie.ui

import academy.android.mymovie.R
import academy.android.mymovie.clickinterface.ActorClickInterface
import academy.android.mymovie.clickinterface.ContainerListener
import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.utils.Constants.ACTOR_KEY
import academy.android.mymovie.utils.Constants.MOVIE_KEY
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), MovieClickInterface, ActorClickInterface,
    ContainerListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}
