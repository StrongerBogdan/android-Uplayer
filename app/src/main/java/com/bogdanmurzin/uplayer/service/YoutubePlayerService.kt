package com.bogdanmurzin.uplayer.service

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import com.bogdanmurzin.uplayer.service.MusicPlayerService.Companion.MEDIA_TAG
import com.bogdanmurzin.uplayer.service.callback.MediaSessionCallback
import com.bogdanmurzin.uplayer.service.notification.MediaNotificationManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class YoutubePlayerService : MediaBrowserServiceCompat() {

    private lateinit var player: YouTubePlayer
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var session: MediaSessionCompat
    private lateinit var stateBuilder: PlaybackStateCompat.Builder
    private lateinit var mMediaNotificationManager: MediaNotificationManager
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

//    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = session

    override fun onCreate() {
        super.onCreate()

        sessionToken = session.sessionToken

        session = MediaSessionCompat(this, MEDIA_TAG).apply {
            // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE)
            setPlaybackState(stateBuilder.build())

            // MySessionCallback() has methods that handle callbacks from a media controller
            setCallback(MediaSessionCallback(player))

            // Set the session's token so that client activities can communicate with it.
            setSessionToken(sessionToken)
        }

        mMediaNotificationManager = MediaNotificationManager(this)
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot("root", null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.sendResult(null)
    }

}