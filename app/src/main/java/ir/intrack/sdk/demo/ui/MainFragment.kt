package ir.intrack.sdk.demo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.card.MaterialCardView
import ir.intrack.sdk.demo.R


class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val eventCard = view.findViewById<MaterialCardView>(R.id.eventsCard)
        eventCard.setOnClickListener {
            view.findNavController().navigate(R.id.nav_events)
        }

        val usersCard = view.findViewById<MaterialCardView>(R.id.userDetailsCard)
        usersCard.setOnClickListener {
            view.findNavController().navigate(R.id.nav_user_details)
        }

        val pushCard = view.findViewById<MaterialCardView>(R.id.pushNotificationCard)
        pushCard.setOnClickListener {
            view.findNavController().navigate(R.id.nav_push_notification)
        }

        val crashCard = view.findViewById<MaterialCardView>(R.id.crashReportCard)
        crashCard.setOnClickListener {
            view.findNavController().navigate(R.id.nav_crash)
        }

        return view
    }
}