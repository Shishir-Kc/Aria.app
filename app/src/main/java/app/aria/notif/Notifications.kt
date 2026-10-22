package app.aria.notif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import app.aria.R

/**
 * All notification plumbing for Aria lives here.
 *
 * v0.1: the only caller is a button in MainActivity.
 * v0.2+: the home-server integration calls [post] from its listener —
 * the signature intentionally never changes.
 */
object Notifications {

    const val CHANNEL_ID = "aria.general"
    const val NOTIFICATION_ID = 1

    /** Kept as a literal: Manifest.permission.POST_NOTIFICATIONS is an API-33 constant. */
    const val POST_PERMISSION = "android.permission.POST_NOTIFICATIONS"

    /**
     * Channels are mandatory from API 26 / Android 8, and immutable once created
     * (importance fixes itself at creation; changing it later needs a new channel).
     * Created with DEFAULT importance: visible in shade, no heads-up takeover.
     */
    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Aria",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply { description = "Notifications delivered by Aria" }
        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    /** On API < 33 notifications post without any permission, so always granted. */
    fun hasPostPermission(context: Context): Boolean =
        Build.VERSION.SDK_INT < 33 ||
            ContextCompat.checkSelfPermission(context, POST_PERMISSION) == PERMISSION_GRANTED

    fun post(context: Context, title: String, body: String) {
        if (!hasPostPermission(context)) return
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_aria) // setSmallIcon is required — null icon throws
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .build()
        context.getSystemService(NotificationManager::class.java)
            .notify(NOTIFICATION_ID, notification)
    }
}
