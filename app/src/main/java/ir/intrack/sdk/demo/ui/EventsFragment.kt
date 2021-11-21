package ir.intrack.sdk.demo.ui

import org.json.JSONObject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

import com.google.android.material.textfield.TextInputLayout

import ir.intrack.sdk.demo.R
import ir.intrack.sdk.demo.toMap

//InTrack
import ir.intrack.android.sdk.InTrack


class EventsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_events, container, false)

        val simpleEvent = view.findViewById<Button>(R.id.sendSimpleEventBtn)
        simpleEvent.setOnClickListener {
            InTrack.recordEvent("simple event")
            val toast = Toast.makeText(view.context, "simple event sent", Toast.LENGTH_SHORT)
            toast.show()
        }

        val eventWithDetails = view.findViewById<Button>(R.id.sendEventWithDataBtn)
        eventWithDetails.setOnClickListener {
            val eventDetails = mapOf(
                "numerical_field" to 123,
                "string_field" to "string",
                "date_field" to  "1970-01-01",
                "array_field" to IntArray(5) { it * 1 },
                "object_field" to mapOf("key1" to "value1", "key2" to "value2"),
            )

            InTrack.recordEvent("detailed event", eventDetails)
            val toast = Toast.makeText(view.context, "detailed event sent", Toast.LENGTH_SHORT)
            toast.show()
        }

        val customEvent = view.findViewById<Button>(R.id.sendCustomEventBtn)
        val eventNameLayout = view.findViewById<TextInputLayout>(R.id.eventName)
        val eventDetailsLayout = view.findViewById<TextInputLayout>(R.id.eventDetails)

        customEvent.setOnClickListener {
            try {
                val eventName: String = eventNameLayout.editText?.text.toString()
                val detailsTxt: String = eventDetailsLayout.editText?.text.toString()
                val eventDetailsMap = toMap(JSONObject(detailsTxt))

                InTrack.recordEvent(eventName, eventDetailsMap)
                val toast = Toast.makeText(view.context, "custom event sent!", Toast.LENGTH_SHORT)
                toast.show()
            } catch (t: Throwable){
                val toast = Toast.makeText(view.context, "input is no valid!", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        return view;
    }

}