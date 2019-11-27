package nl.guldem.mylittlebunnie.ui.video

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.navigation.fragment.navArgs
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_video_player.*
import nl.guldem.mylittlebunnie.R
import nl.guldem.mylittlebunnie.util.MyBunnieFragment
import nl.guldem.mylittlebunnie.util.PlayerHolder
import nl.guldem.mylittlebunnie.util.PlayerState
import nl.guldem.mylittlebunnie.util.extensions.addOnAnimationEnded

@Parcelize
data class VideoArgs(
    val videoUri: Uri,
    val present: String
    ): Parcelable

class VideoFragment : MyBunnieFragment() {
    override val layout = R.layout.fragment_video_player
    private val playerState = PlayerState()
    private lateinit var player: PlayerHolder
    private val args: VideoFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val source = args.source

        player = PlayerHolder(requireContext(), video_player, playerState.copy(source = source.videoUri))

        suprise_present_image.setAnimation(source.present)
        suprise_present_image.setMaxProgress(0.6f)
        suprise_present_image.playAnimation()
        suprise_present_image.addOnAnimationEnded {
            overlay_view.animate().alpha(0f).setDuration(1000).withEndAction {
                overlay_view?.visibility = View.GONE
                video_player?.visibility = View.VISIBLE
                player?.start()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        player.stop()
    }


    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}