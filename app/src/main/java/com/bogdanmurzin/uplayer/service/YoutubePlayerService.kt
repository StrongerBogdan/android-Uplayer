package com.bogdanmurzin.uplayer.service

import android.app.Service
import android.content.Intent
import android.media.session.PlaybackState
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.KeyEvent
import androidx.lifecycle.Lifecycle
import androidx.media.session.MediaButtonReceiver
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.BuildConfig
import com.bogdanmurzin.uplayer.common.Constants
import com.bogdanmurzin.uplayer.common.Constants.NOTIFICATION_MUSIC_ID
import com.bogdanmurzin.uplayer.service.callback.MediaSessionCallback
import com.bogdanmurzin.uplayer.service.notification.MediaNotificationManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class YoutubePlayerService : Service(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    var mMediaSessionCompat: MediaSessionCompat? = null
        private set
    private lateinit var playbackState: PlaybackStateCompat
    private val binder = LocalBinder()
    private lateinit var notificationManager: MediaNotificationManager

    private var player: YouTubePlayer? = null
    private var currentVideoItem: VideoItem? = null
    private var isPlay = true

    override fun onCreate() {
        super.onCreate()

        mMediaSessionCompat = MediaSessionCompat(this, MEDIA_TAG)

        playbackState = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or
                        PlaybackStateCompat.ACTION_PAUSE or
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                        PlaybackStateCompat.ACTION_SEEK_TO
            )
            .setState(
                PlaybackStateCompat.STATE_PLAYING,
                PlaybackState.PLAYBACK_POSITION_UNKNOWN,
                0f
            )
            .build()
        mMediaSessionCompat?.setPlaybackState(playbackState)
        mMediaSessionCompat?.isActive = true

        notificationManager = MediaNotificationManager(this)
    }

    fun setPlayer(youTubePlayer: YouTubePlayer) {
        player = youTubePlayer
        mMediaSessionCompat?.setCallback(MediaSessionCallback(youTubePlayer))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            MediaButtonReceiver.handleIntent(mMediaSessionCompat, intent)
            if (intent?.action != null && intent.action == ACTION_STOP_FOREGROUND) {
                stopForegroundService()
            }
            if (Intent.ACTION_MEDIA_BUTTON == intent?.action) {
                val keyEvent: KeyEvent? = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT)
                if (keyEvent?.keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                    if (isPlay) {
                        player?.pause()
                        isPlay = false
                        Log.i(Constants.TAG, "keyEvent pause")
                    } else {
                        player?.play()
                        isPlay = true
                        Log.i(Constants.TAG, "keyEvent play")
                    }
                }
            }
            currentVideoItem?.let {
                val notification = notificationManager.getNotification(
                    it,
                    playbackState
                )
                startForeground(NOTIFICATION_MUSIC_ID, notification)
            }
        }
        return START_STICKY
    }

    fun loadVideo(lifecycle: Lifecycle, video: VideoItem) {
        currentVideoItem = video
        isPlay = true
        player?.loadOrCueVideo(lifecycle, video.videoId, 0f)
    }

    private fun stopForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            stopForeground(true)
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onBind(intent: Intent?): IBinder = binder

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): YoutubePlayerService = this@YoutubePlayerService
    }

    companion object {
        const val ACTION_STOP_FOREGROUND = "${BuildConfig.APPLICATION_ID}.stopforeground"
        val MEDIA_TAG: String = MusicPlayerService::class.java.simpleName
    }
}
