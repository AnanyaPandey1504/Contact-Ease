package com.example.contactsapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CallLog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CallLogsFragment : Fragment() {

    private lateinit var callLogsRecyclerView: RecyclerView
    private lateinit var callLogsAdapter: CallLogsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_call_logs, container, false)
        callLogsRecyclerView = view.findViewById(R.id.callLogsRecyclerView)
        callLogsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALL_LOG)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.READ_CALL_LOG), 1)
        } else {
            fetchCallLogs()
        }
        return view
    }

    @SuppressLint("Range")
    private fun fetchCallLogs() {
        val callLogsList = mutableListOf<CustomCallLog>()
        val cursor = requireContext().contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            CallLog.Calls.DATE + " DESC"
        )

        cursor?.let {
            while (it.moveToNext()) {
                val number = it.getString(it.getColumnIndex(CallLog.Calls.NUMBER))
                val name = it.getString(it.getColumnIndex(CallLog.Calls.CACHED_NAME)) ?: "Unknown"
                val dateTimestamp = it.getLong(it.getColumnIndex(CallLog.Calls.DATE))
                val callType = it.getInt(it.getColumnIndex(CallLog.Calls.TYPE))

                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dateTimestamp))
                val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(dateTimestamp))

                val callLogItem = CustomCallLog(name, number, date, callType, time, callType)
                callLogsList.add(callLogItem)
            }
        }
        cursor?.close()

        callLogsAdapter = CallLogsAdapter(callLogsList) { callLog ->

            Toast.makeText(requireContext(), "Clicked on: ${callLog.name}", Toast.LENGTH_SHORT).show()
        }
        callLogsRecyclerView.adapter = callLogsAdapter
    }
}



