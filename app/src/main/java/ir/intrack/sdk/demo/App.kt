package ir.intrack.sdk.demo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import ir.intrack.android.sdk.InTrack
import ir.intrack.android.sdk.InTrackConfig
import ir.intrack.android.sdk.InTrackPush
import ir.intrack.android.sdk.InTrackPush.INTRACK_BROADCAST_PERMISSION_POSTFIX

class App : Application() {

    private var messageReceiver: BroadcastReceiver? = null

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_DEFAULT)
            )
        }

        //InTrack initialization:
        val inTrackAppKey = "APP_KEY"
        val inTrackAuthKey = "AUTH_KEY"
        InTrack.init(
            InTrackConfig()
                .setApplication(this)
                .setAppKey(inTrackAppKey)
                .setAuthKey(inTrackAuthKey)
                .enableCrashReporting() //enabling crash reports
                .setLoggingEnabled(true) //enabling logging
        )


        /* Register for broadcast action if you need to be notified when InTrack message clicked */
        messageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val sentIntent = intent.getParcelableExtra<Intent>(InTrackPush.EXTRA_INTENT)
                sentIntent!!.setExtrasClassLoader(InTrackPush::class.java.classLoader)
                val bun = sentIntent.getParcelableExtra<Bundle>(InTrackPush.EXTRA_MESSAGE)
                val message: InTrackPush.Message?
                val actionIndex = sentIntent.getIntExtra(InTrackPush.EXTRA_ACTION_INDEX, -100)
                var title = "NULL"
                if (bun != null) {
                    message = bun.getParcelable(InTrackPush.EXTRA_MESSAGE)
                    if (message != null) {
                        title = message.message()
                    }
                }
                Log.i("InTrack", "[Activity] Got a message with title :[$title]")
            }
        }

        val filter = IntentFilter()
        filter.addAction(InTrackPush.SECURE_NOTIFICATION_BROADCAST)
        registerReceiver(
            messageReceiver,
            filter,
            packageName + INTRACK_BROADCAST_PERMISSION_POSTFIX,
            null
        )
    }
}
