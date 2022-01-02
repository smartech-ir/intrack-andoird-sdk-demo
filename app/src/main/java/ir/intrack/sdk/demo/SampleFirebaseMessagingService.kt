package ir.intrack.sdk.demo

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ir.intrack.android.sdk.InTrack
import ir.intrack.android.sdk.InTrackPush

class SampleFirebaseMessagingService : FirebaseMessagingService()  {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        val message = InTrackPush.decodeMessage(remoteMessage.data)
        var intent: Intent? = null

        if(message.customData().containsKey("key1")){
            //do something with your custom key-value pairs data...
            intent = Intent(applicationContext, MainActivity::class.java)
        }

        val result = InTrack.displayMessage(applicationContext, remoteMessage.data, getString(R.string.default_notification_channel_id), R.mipmap.ic_launcher, intent)

        if (result == null) {
            Log.i(TAG, "Message doesn't have anything to display or wasn't sent from InTrack, so it cannot be handled by InTrack SDK");
        } else if (result) {
            Log.i(TAG, "Message was handled by InTrack SDK");
        } else {
            Log.i(TAG, "Message wasn't handled by InTrack SDK because API level is too low for Notification support or because currentActivity is null (not enough lifecycle method calls)");
        }
    }

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        //send the FCM registration token to InTrack server.
        InTrack.onTokenRefresh(token)
    }

    companion object {

        private const val TAG = "SampleFirebaseService"
    }

}