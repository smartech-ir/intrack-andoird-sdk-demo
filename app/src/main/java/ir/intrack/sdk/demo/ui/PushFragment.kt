package ir.intrack.sdk.demo.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

import ir.intrack.sdk.demo.R

import ir.intrack.android.sdk.InTrack
import ir.intrack.sdk.demo.PushSampleActivity

class PushFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_push, container, false)

        view.findViewById<Button>(R.id.initNotification).setOnClickListener {
            //init InTrack notification service
            InTrack.initPush(this.requireActivity().application)

            //getting fcm token and send it to InTrack
            Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    //Fetching FCM registration token failed
                    val toast = Toast.makeText(view.context, "fcm registration failed", Toast.LENGTH_SHORT)
                    toast.show()
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                //Registering token on InTrack
                InTrack.onTokenRefresh(token)

                view.findViewById<TextInputLayout>(R.id.fmcPushToken).editText?.setText(token)

                val toast = Toast.makeText(view.context, "token sent to InTrack", Toast.LENGTH_SHORT)
                toast.show()
            })
        }

        view.findViewById<Button>(R.id.samplePush).setOnClickListener {
            val message = mapOf(  "id" to "messageId01",
                "title" to "Push Title",
                "message" to  "Push Message",
//                "sound" to "",
//                "link" to "",
                "media" to "https://docs.intrack.ir/img/logo.png",
//                "buttons" to "[{\"t\":\"google\",\"l\":\"https:\\/\\/google.com\"},{\"t\":\"facebook\",\"l\":\"https:\\/\\/facebook.com\"}]"
            )

            val intent = Intent(this.requireActivity().application, PushSampleActivity::class.java)

            InTrack.displayMessage(
                this.requireActivity().application,
                message,
                getString(R.string.default_notification_channel_id),
                R.drawable.ic_launcher_foreground,
                intent
            )

        }

        view.findViewById<Button>(R.id.triggerPushEvent).setOnClickListener {
            InTrack.recordEvent("event push trigger")
        }

        return view
    }
}