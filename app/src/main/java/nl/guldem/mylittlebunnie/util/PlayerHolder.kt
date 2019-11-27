package nl.guldem.mylittlebunnie.util

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import nl.guldem.mylittlebunnie.ui.home.Presents

data class PlayerState(
    var window: Int = 0,
    var position: Long = 0,
    var whenReady: Boolean = true,
    var source: Uri = Presents.PresentOne.source
)

class PlayerHolder(
    val context: Context,
    val playerView: PlayerView,
    val playerState: PlayerState
) {
    val player: ExoPlayer

    init {
        // Create the player instance.
        player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
            .also {
                playerView.player = it // Bind to the view.
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL)

            }
        player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
    }

    fun start() {
        // Load media.
        player.prepare(buildMediaSource(playerState.source))
        with(playerState) {
            // Start playback when media has buffered enough.
            player.playWhenReady = true
            player.seekTo(window, position)

        }



    }

    private fun buildMediaSource(uri: Uri): ProgressiveMediaSource {
        return ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(context, "videoapp")
        ).createMediaSource(uri)
    }

    fun stop() {
        with(player) {
            // Save state
            with(playerState) {
                position = currentPosition
                window = currentWindowIndex
                whenReady = playWhenReady
            }
            // Stop the player (and release it's resources). The player instance can be reused.
            stop(true)
        }
    }

    fun release() {
        player.release()
    }


}