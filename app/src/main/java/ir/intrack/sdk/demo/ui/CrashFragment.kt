package ir.intrack.sdk.demo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

import ir.intrack.sdk.demo.R

//InTrack
import ir.intrack.android.sdk.InTrack
import java.lang.Exception


class CrashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crash, container, false)

        val handledException = view.findViewById<Button>(R.id.recordHandledExceptionBtn)
        handledException.setOnClickListener {
            InTrack.recordHandledException(Exception("handled exception"))
            val toast = Toast.makeText(view.context, "exception recorded", Toast.LENGTH_SHORT)
            toast.show()
        }

        val unHandledException = view.findViewById<Button>(R.id.recordUnhandledExceptionBtn)
        unHandledException.setOnClickListener {
            InTrack.recordUnhandledException(Exception("handled (fatal) exception"))
            val toast = Toast.makeText(view.context, "fatal exception recorded", Toast.LENGTH_SHORT)
            toast.show()
        }

        val exceptionBtn = view.findViewById<Button>(R.id.throwExceptionBtn)
        exceptionBtn.setOnClickListener {
            throw Exception("app crashed");
        }

        return view;
    }

}