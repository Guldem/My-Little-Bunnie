package nl.guldem.mylittlebunnie.ui.home

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_home.*
import nl.guldem.mylittlebunnie.R
import nl.guldem.mylittlebunnie.ui.supprise.SupriseListFragment
import nl.guldem.mylittlebunnie.ui.video.VideoArgs
import nl.guldem.mylittlebunnie.util.MyBunnieFragment
import nl.guldem.mylittlebunnie.util.extensions.addOnPageSelectedListener
import nl.guldem.mylittlebunnie.util.extensions.findNavController
import nl.guldem.mylittlebunnie.util.extensions.toBundle
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : MyBunnieFragment() {
    override val layout = R.layout.fragment_home
    val model: HomeViewModel by viewModel()
    private var currentPosition: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("test-pager", "onViewCreated currentPos: $currentPosition")

        home_pager.addOnPageSelectedListener {
            model.setPosition(it)
        }

        model.init()
        Log.d("test-pager", "onViewCreated model.init")

        model.suprisesViewState.observeNotNull {
            home_pager.adapter = SupriseViewPageAdapter(childFragmentManager, it.suprises)
            home_pager.currentItem = currentPosition
            Log.d("test-pager", "model observe currentPos: $currentPosition")
        }

        model.suprisesPosition.observeNotNull {
            currentPosition = it
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("test-pager", "onResume currentPos: $currentPosition")
        model.refresh()
    }

    fun navigateToVideoPlayer(source: VideoArgs) {
        currentPosition = home_pager.currentItem
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToVideoFragment(source))
    }
}

private class SupriseViewPageAdapter(fm: FragmentManager, private val suprises: List<Suprise>) :
    FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int = suprises.size

    override fun getItem(position: Int): Fragment = SupriseListFragment().apply {
        arguments = SupriseArgs(suprise = suprises[position], position = position).toBundle()
    }
}

@Parcelize
data class SupriseArgs(
    val suprise: Suprise,
    val position: Int
) : Parcelable