package io.github.ashutoshgngwr.exoplayerdemo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.EventLogger

class MainActivity : AppCompatActivity(), Player.Listener {

    private val trackSelector by lazy {
        DefaultTrackSelector(this, AdaptiveTrackSelection.Factory())
    }

    private val exoPlayer by lazy {
        ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .setTrackSelector(trackSelector)
            .build()
    }

    private lateinit var playbackState: TextView
    private lateinit var isPlaying: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playbackState = findViewById(R.id.playbackState)
        isPlaying = findViewById(R.id.isPlaying)
        exoPlayer.addListener(this)
        exoPlayer.addAnalyticsListener(EventLogger(trackSelector))

        // DASH
        exoPlayer.addMediaItem(MediaItem.fromUri("https://cdn.trynoice.com/public/dash/white_noise_white_noise/index.mpd"))
        exoPlayer.addMediaItem(MediaItem.fromUri("https://cdn.trynoice.com/public/dash/white_noise/index.mpd"))

        // HLS
        // exoPlayer.addMediaItem(MediaItem.fromUri("https://cdn.trynoice.com/public/hls/white_noise_white_noise/index.m3u8"))
        // exoPlayer.addMediaItem(MediaItem.fromUri("https://cdn.trynoice.com/public/hls/white_noise/index.m3u8"))

        // Progressive
        // exoPlayer.addMediaItem(MediaItem.fromUri("https://cdn.trynoice.com/public/white_noise_white_noise.mp3"))
        // exoPlayer.addMediaItem(MediaItem.fromUri("https://cdn.trynoice.com/public/white_noise.mp3"))

        exoPlayer.playWhenReady = true
        exoPlayer.prepare()
    }

    override fun onDestroy() {
        exoPlayer.release()
        super.onDestroy()
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        this.isPlaying.text = if (isPlaying) "playing" else "not playing"
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        this.playbackState.text = when (playbackState) {
            Player.STATE_BUFFERING -> "buffering"
            Player.STATE_ENDED -> "ended"
            Player.STATE_IDLE -> "idle"
            Player.STATE_READY -> "ready"
            else -> "unknown"
        }
    }
}
