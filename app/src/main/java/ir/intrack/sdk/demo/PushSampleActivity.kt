package ir.intrack.sdk.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

//InTrack
import ir.intrack.android.sdk.InTrackPush


class PushSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bun = intent?.getParcelableExtra<Bundle>(InTrackPush.EXTRA_MESSAGE)
        val message: InTrackPush.Message?
        if (bun != null) {
            message = bun.getParcelable(InTrackPush.EXTRA_MESSAGE)
            if (message != null) {
                Toast.makeText(this, message.title(), Toast.LENGTH_SHORT).show()
            }
        }

        setContentView(R.layout.activity_push_sample)
    }
}