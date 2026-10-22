package app.aria

import android.app.Application
import app.aria.notif.Notifications

/** Creates the notification channel once, at process start — before any UI exists. */
class AriaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Notifications.ensureChannel(this)
    }
}
