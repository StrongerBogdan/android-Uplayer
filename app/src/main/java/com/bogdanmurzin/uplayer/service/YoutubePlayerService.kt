package com.bogdanmurzin.uplayer.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.view.KeyEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media.session.MediaButtonReceiver
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.BuildConfig
import com.bogdanmurzin.uplayer.common.Constants.NOTIFICATION_MUSIC_ID
import com.bogdanmurzin.uplayer.model.playlist.PlayList
import com.bogdanmurzin.uplayer.service.notification.MediaNotificationManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class YoutubePlayerService : Service(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var mMediaSessionCompat: MediaSessionCompat? = null
    private val playbackState = YouTubePlayerTracker()
    private val binder = LocalBinder()
    private lateinit var notificationManager: MediaNotificationManager

    private var player: YouTubePlayer? = null

    // Current loaded video and playlist
    private var currentVideoItem: VideoItem? = null
    private var currentPlaylist: PlayList? = null

    // Livedata for updating UI in nowPlaying
    private val _currentVideo: MutableLiveData<VideoItem> = MutableLiveData()
    var currentVideo: LiveData<VideoItem> = _currentVideo

    // Player state change listener
    private val listener = object : AbstractYouTubePlayerListener() {
        override fun onStateChange(
            youTubePlayer: YouTubePlayer,
            state: PlayerConstants.PlayerState
        ) {
            super.onStateChange(youTubePlayer, state)
            if (state == PlayerConstants.PlayerState.ENDED) {
                next()
            }
            if (state == PlayerConstants.PlayerState.PAUSED ||
                state == PlayerConstants.PlayerState.PLAYING ||
                state == PlayerConstants.PlayerState.ENDED
            ) {
                Log.i(TAG, "onStateChange: $state")
                startService()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        mMediaSessionCompat = MediaSessionCompat(this, MEDIA_TAG)
        mMediaSessionCompat?.isActive = true

        notificationManager = MediaNotificationManager(this)

        return binder
    }

    fun setPlayer(youTubePlayer: YouTubePlayer) {
        player = youTubePlayer
        youTubePlayer.addListener(listener)
        youTubePlayer.addListener(playbackState)
    }

    @Suppress("DEPRECATION")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            MediaButtonReceiver.handleIntent(mMediaSessionCompat, intent)
            if (intent?.action != null && intent.action == ACTION_STOP_FOREGROUND) {
                stopForegroundService()
            }
            if (Intent.ACTION_MEDIA_BUTTON == intent?.action) {
                val keyEvent: KeyEvent? = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT)
                when (keyEvent?.keyCode) {
                    KeyEvent.KEYCODE_MEDIA_NEXT -> next().also { Log.i(TAG, "keyEvent next") }
                    KeyEvent.KEYCODE_MEDIA_PREVIOUS -> prev().also { Log.i(TAG, "keyEvent prev") }
                    KeyEvent.KEYCODE_MEDIA_PLAY -> play().also { Log.i(TAG, "keyEvent play") }
                    KeyEvent.KEYCODE_MEDIA_PAUSE -> pause().also { Log.i(TAG, "keyEvent pause") }
                }
                startService()
            }
        }
        return START_STICKY
    }

    private fun prev() {
        currentPlaylist?.prevVideo()?.let {
            loadVideo(it)
        }
    }

    private fun next() {
        currentPlaylist?.nextVideo()?.let {
            loadVideo(it)
        }
    }

    private fun startService() {
        serviceScope.launch {
            currentVideoItem?.let {
                val notification = notificationManager.getNotification(
                    it,
                    playbackState.state
                )
                startForeground(NOTIFICATION_MUSIC_ID, notification)
            }
        }
    }

    fun loadPlaylist(playlist: PlayList) {
        currentPlaylist = playlist
        playlist.nextVideo()?.let {
            loadVideo(it)
        }
    }

    fun loadVideo(video: VideoItem) {
        currentVideoItem = video
        _currentVideo.postValue(video)
        player?.loadVideo(video.videoId, 0f)
    }

    @Suppress("DEPRECATION")
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

    private fun play() {
        player?.play()
    }

    private fun pause() {
        player?.pause()
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): YoutubePlayerService = this@YoutubePlayerService
    }

    companion object {
        const val ACTION_STOP_FOREGROUND = "${BuildConfig.APPLICATION_ID}.stopforeground"
        val MEDIA_TAG: String = MusicPlayerService::class.java.simpleName
        const val TAG = "YTServiceDebug"
    }
}
