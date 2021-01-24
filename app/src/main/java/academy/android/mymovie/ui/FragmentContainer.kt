package academy.android.mymovie.ui

import academy.android.mymovie.adapter.FragmentPagerAdapter
import academy.android.mymovie.databinding.FragmentContainerBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class FragmentContainer : Fragment() {

    private var _binding: FragmentContainerBinding? = null
    private val binding get() = _binding!!

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}