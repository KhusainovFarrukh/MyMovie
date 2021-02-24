package academy.android.mymovie.ui

import academy.android.mymovie.adapters.FragmentPagerAdapter
import academy.android.mymovie.clickinterfaces.ContainerListener
import academy.android.mymovie.databinding.FragmentContainerBinding
import academy.android.mymovie.utils.Constants.TITLE_NOWPLAYING
import academy.android.mymovie.utils.Constants.TITLE_POPULAR
import academy.android.mymovie.utils.Constants.TITLE_TOPRATED
import academy.android.mymovie.utils.Constants.TITLE_UPCOMING
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class FragmentContainer : Fragment() {

    private var _binding: FragmentContainerBinding? = null
    private val binding get() = _binding!!
    private var containerListener: ContainerListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContainerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpFragments.adapter = FragmentPagerAdapter(this)
        TabLayoutMediator(binding.tlTitles, binding.vpFragments) { tab, position ->
            when (position) {
                0 -> tab.text = TITLE_POPULAR
                1 -> tab.text = TITLE_TOPRATED
                2 -> tab.text = TITLE_NOWPLAYING
                3 -> tab.text = TITLE_UPCOMING
            }
        }.attach()

        binding.imvSearch.setOnClickListener {
            containerListener?.addSearchFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContainerListener) {
            containerListener = context
        } else {
            throw IllegalArgumentException("$context is not ContainerListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        containerListener = null
    }
}