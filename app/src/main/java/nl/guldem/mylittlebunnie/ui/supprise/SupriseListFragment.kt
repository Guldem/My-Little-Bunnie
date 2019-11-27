package nl.guldem.mylittlebunnie.ui.supprise

import android.os.Bundle
import android.view.View
import android.view.animation.CycleInterpolator
import android.widget.Toast
import kotlinx.android.synthetic.main.view_suprise_layout.*
import nl.guldem.mylittlebunnie.R
import nl.guldem.mylittlebunnie.ui.home.HomeFragment
import nl.guldem.mylittlebunnie.ui.home.Suprise
import nl.guldem.mylittlebunnie.ui.home.SupriseArgs
import nl.guldem.mylittlebunnie.ui.video.VideoArgs
import nl.guldem.mylittlebunnie.util.MyBunnieFragment
import nl.guldem.mylittlebunnie.util.extensions.formatDateString
import nl.guldem.mylittlebunnie.util.extensions.nowInAmsterdam
import nl.guldem.mylittlebunnie.util.extensions.onClick
import nl.guldem.mylittlebunnie.util.extensions.readParcelable


class SupriseListFragment : MyBunnieFragment() {
    override val layout = R.layout.view_suprise_layout
    private var position: Int = 0
    private lateinit var parent: HomeFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parent = this.parentFragment as HomeFragment
        val surpirseArgs = arguments.readParcelable<SupriseArgs>()
        position = surpirseArgs?.position ?: 0

        surpirseArgs?.suprise?.let { suprise ->
            showSuprise(suprise)

            parent.model.suprisesPosition.observeNotNull {
                if (it == position && !suprise.opened) {
                    wiggleAnimation()
                }
            }
        }

    }

    private fun showSuprise(suprise: Suprise) {
        suprise_date.text = suprise.date.formatDateString()

        suprise_present_image.setAnimation(suprise.presentImage)
        suprise_present_image.progress = if (suprise.opened) 0.6f else 0f

        if (suprise.date <= nowInAmsterdam()) {
            suprise_open_me.text =
                if (suprise.opened) {
                    requireContext().getString(R.string.suprise_replay)
                } else {
                    requireContext().getString(R.string.suprise_open_me)
                }
            suprise_open_me.visibility = View.VISIBLE
            suprise_open_me.onClick { openPresent(suprise) }

            suprise_present_image.setMaxProgress(0.6f)
            suprise_present_image.onClick {
               openPresent(suprise)
            }
        } else {
            suprise_present_image.onClick {
                Toast.makeText(requireContext(), "Not today!", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun openPresent(suprise: Suprise){
        parent.model.openPresentWithViewUpdate(suprise.id)
        parent.navigateToVideoPlayer(VideoArgs(suprise.supriseVideo, suprise.presentImage))
    }

    private fun wiggleAnimation() {
        suprise_present_image.animate().setInterpolator(CycleInterpolator(3f)).rotation(5f).setDuration(500).start()
    }

}

