package com.bogdanmurzin.uplayer.service

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.view.KeyEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media.session.MediaButtonReceiver
import com.bogdanmurzin.domain.entities.Music
import com.bogdanmurzin.uplayer.BuildConfig
import com.bogdanmurzin.uplayer.common.Constants.NOTIFICATION_MUSIC_ID
import com.bogdanmurzin.uplayer.common.PlayerConstants.PlayerState
import com.bogdanmurzin.uplayer.model.*
import com.bogdanmurzin.uplayer.model.player.*
import com.bogdanmurzin.uplayer.model.playlist.PlayList
import com.bogdanmurzin.uplayer.service.notification.MediaNotificationManager
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

    private var mPlayer: Player? = null

    private val mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    // Livedata for updating UI in nowPlaying
    private val _currentVideo: MutableLiveData<Music> = MutableLiveData()
    var currentVideo: LiveData<Music> = _currentVideo
    private val _playingState: MutableLiveData<Boolean> = MutableLiveData()
    var playingState: LiveData<Boolean> = _playingState

    // Player state change listener
    private val listener = object : CustomPlayerListener {
        override fun onStateChange(
            music: Music,
            state: PlayerState
        ) {
            when (state) {
                PlayerState.ENDED -> next()
                PlayerState.PAUSED, PlayerState.PLAYING -> {
                    Log.i(TAG, "onStateChange: music ${music.title}, state $state")
                    val isPlaying = state == PlayerState.PLAYING
                    _playingState.postValue(isPlaying)
                    startService(music, isPlaying)
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        mMediaSessionCompat = MediaSessionCompat(this, MEDIA_TAG)
        mMediaSessionCompat?.isActive = true

        notificationManager = MediaNotificationManager(this)

        return binder
    }

    fun setPlayer(playerType: PlayerType) {
        if (mPlayer != null) mPlayer?.stop()
        mPlayer = when (playerType) {
            is PlayerType.YTPlayer -> {
                // TODO MB redundant line below
                playerType.youtubePlayer.addListener(playbackState)
                VideoPlayer(playerType.youtubePlayer)
            }
            is PlayerType.LocalPlayer -> AudioPlayer(mediaPlayer, this.applicationContext)
        }
        mPlayer?.addListener(listener)

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
            }
        }
        return START_STICKY
    }

    fun prev() {
        mPlayer?.prev()?.let {
            _currentVideo.postValue(it)
        }
    }

    fun next() {
        mPlayer?.next()?.let {
            _currentVideo.postValue(it)
        }
    }

    private fun startService(music: Music, isPlaying: Boolean) {
        serviceScope.launch {
            val notification = notificationManager.getNotification(music, isPlaying)
            startForeground(NOTIFICATION_MUSIC_ID, notification)
        }
    }

    fun loadPlaylist(playlist: PlayList) {
        mPlayer?.let {
            _currentVideo.postValue(it.loadPlaylist(playlist))
        }
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
        mPlayer?.play()
    }

    private fun pause() {
        mPlayer?.pause()
    }

    fun playPause() {
        playingState.value?.let { isPlaying ->
            if (isPlaying) {
                pause()
            } else {
                play()
            }
        }
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
