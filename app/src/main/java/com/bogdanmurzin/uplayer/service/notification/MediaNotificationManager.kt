package com.bogdanmurzin.uplayer.service.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.session.MediaButtonReceiver
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.R
import com.bogdanmurzin.uplayer.service.YoutubePlayerService
import com.bogdanmurzin.uplayer.ui.MainActivity
import com.bumptech.glide.Glide


class MediaNotificationManager(private val service: YoutubePlayerService) {

    private var mNotificationManager: NotificationManager =
        (service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)

    private val mPlayAction: NotificationCompat.Action =
        NotificationCompat.Action(
            R.drawable.play_icon,
            service.getString(R.string.play),
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                service,
                PlaybackStateCompat.ACTION_PLAY
            )
        )
    private val mPauseAction: NotificationCompat.Action =
        NotificationCompat.Action(
            R.drawable.pause_icon,
            service.getString(R.string.pause),
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                service,
                PlaybackStateCompat.ACTION_PAUSE
            )
        )
    private val mNextAction: NotificationCompat.Action =
        NotificationCompat.Action(
            R.drawable.next_icon,
            service.getString(R.string.next),
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                service,
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            )
        )
    private val mPrevAction: NotificationCompat.Action =
        NotificationCompat.Action(
            R.drawable.previous,
            service.getString(R.string.previous),
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                service,
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            )
        )

    init {
        // Cancel all notifications to handle the case where the Service was killed and
        // restarted by the system.
        mNotificationManager.cancelAll()
    }

    fun getNotification(
        videoData: VideoItem,
        state: PlaybackStateCompat,
        token: MediaSessionCompat.Token
    ): Notification {
        val isPlaying = state.state == PlaybackStateCompat.STATE_PLAYING
        val builder: NotificationCompat.Builder =
            buildNotification(state, token, isPlaying, videoData)
        return builder.build()
    }

    private fun buildNotification(
        state: PlaybackStateCompat,
        token: MediaSessionCompat.Token,
        isPlaying: Boolean,
        videoData: VideoItem
    ): NotificationCompat.Builder {

        // Create the (mandatory) notification channel when running on Android Oreo.
        if (isAndroidOOrHigher()) createChannel()

        val builder = NotificationCompat.Builder(service, CHANNEL_ID)
        builder.setStyle(
            androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(token)
                .setShowActionsInCompactView(0, 1, 2) // Compatibility with Android L and earlier.
                .setShowCancelButton(true)
                .setCancelButtonIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        service,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )
        )
            .setColor(ContextCompat.getColor(service, R.color.gray))
            .setSmallIcon(R.drawable.no_item_icon) // Pending intent that is fired when user clicks on notification.
            .setContentIntent(createContentIntent()) // Title - Usually Song name.
            .setContentTitle(videoData.title) // Subtitle - Usually Artist name.
            .setContentText(videoData.author)
            .setLargeIcon(getBitmap(videoData.imageUrl))
            // When notification is deleted (when playback is paused and notification can be
            // deleted) fire MediaButtonPendingIntent with ACTION_STOP.
            .setDeleteIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    service, PlaybackStateCompat.ACTION_STOP
                )
            ) // Show controls on lock screen even when user hides sensitive content.
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        // If skip to next action is enabled.
        if (state.actions and PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS != 0L) {
            builder.addAction(mPrevAction)
        }
        builder.addAction(if (isPlaying) mPauseAction else mPlayAction)

        // If skip to prev action is enabled.
        if (state.actions and PlaybackStateCompat.ACTION_SKIP_TO_NEXT != 0L) {
            builder.addAction(mNextAction)
        }
        return builder
    }

    // Does nothing on versions of Android earlier than O.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        if (mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            // The user-visible name of the channel.
            val name: CharSequence = "MediaSession"
            // The user-visible description of the channel.
            val description = "MediaSession and MediaPlayer"
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            // Configure the notification channel.
            mChannel.description = description
            mChannel.enableLights(true)
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mNotificationManager.createNotificationChannel(mChannel)
            Log.d(TAG, "createChannel: New channel created")
        } else {
            Log.d(TAG, "createChannel: Existing channel reused")
        }
    }

    private fun isAndroidOOrHigher(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createContentIntent(): PendingIntent? {
        val openUI = Intent(service, MainActivity::class.java)
        openUI.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(
            service, REQUEST_CODE, openUI, PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    private fun getBitmap(imageUrl: String): Bitmap {
        return Glide.with(service).asBitmap().load(imageUrl).submit(64, 64).get()
    }

    companion object {
        const val NOTIFICATION_ID = 412

        private val TAG = MediaNotificationManager::class.java.simpleName
        private const val CHANNEL_ID = "com.example.android.musicplayer.channel"
        private const val REQUEST_CODE = 501
    }
}