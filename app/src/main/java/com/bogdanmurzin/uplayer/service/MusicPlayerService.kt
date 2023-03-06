package com.bogdanmurzin.uplayer.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.session.PlaybackState
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.KeyEvent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.media.session.MediaButtonReceiver
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.BuildConfig
import com.bogdanmurzin.uplayer.R
import com.bogdanmurzin.uplayer.common.Constants.NOTIFICATION_CHANNEL_ID
import com.bogdanmurzin.uplayer.common.Constants.NOTIFICATION_CHANNEL_NAME
import com.bogdanmurzin.uplayer.common.Constants.NOTIFICATION_MUSIC_ID
import com.bogdanmurzin.uplayer.common.Constants.TAG
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
class MusicPlayerService : Service(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job
    private lateinit var mMediaSessionCompat: MediaSessionCompat
    private val binder = LocalBinder()
    private lateinit var currentVideoItem: VideoItem
    private var isPlay = true

    // Registered callbacks
    private var player: YouTubePlayer? = null

    override fun onBind(intent: Intent?): IBinder {
        mMediaSessionCompat = MediaSessionCompat(this, MEDIA_TAG)

        val state = PlaybackStateCompat.Builder()
            .setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PAUSE)
            .setState(
                PlaybackStateCompat.STATE_STOPPED,
                PlaybackState.PLAYBACK_POSITION_UNKNOWN,
                0f
            )
            .build()
        mMediaSessionCompat.setPlaybackState(state)
        return binder
    }

    fun setPlayer(youTubePlayer: YouTubePlayer) {
        player = youTubePlayer
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        launch {
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
                        Log.i(TAG, "keyEvent pause")
                    } else {
                        player?.play()
                        isPlay = true
                        Log.i(TAG, "keyEvent play")
                    }
                }
            }
            generateNotification()
        }
        return START_STICKY
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun generateNotification() {
        // NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
                )
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }

        val notification = getNotificationBuilder().build()
        startForeground(NOTIFICATION_MUSIC_ID, notification)
    }

    // Notification builder
    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val playPauseDrawer = if (isPlay) R.drawable.pause_icon else R.drawable.play_icon

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.no_item_icon)
            .setContentTitle(currentVideoItem.title)
            .setContentText(currentVideoItem.author)
            .setLargeIcon(getBitmap(currentVideoItem.coverArtUri))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(
                NotificationCompat.Action(
                    playPauseDrawer,
                    getString(R.string.play),
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        this,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE
                    )
                )
            )
    }

    private fun stopForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            stopForeground(true)
        }
        stopSelf()
    }

    fun loadVideo(lifecycle: Lifecycle, video: VideoItem) {
        currentVideoItem = video
        isPlay = true
        player?.loadOrCueVideo(lifecycle, currentVideoItem.videoId, 0f)
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }

    private fun getBitmap(imageUrl: String): Bitmap {
        return Glide.with(this).asBitmap().load(imageUrl).submit(64, 64).get()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        const val ACTION_STOP_FOREGROUND = "${BuildConfig.APPLICATION_ID}.stopforeground"
        val MEDIA_TAG: String = MusicPlayerService::class.java.simpleName
    }
}