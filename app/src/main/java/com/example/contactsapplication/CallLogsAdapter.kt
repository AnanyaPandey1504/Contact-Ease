package com.example.contactsapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CallLogsAdapter(
    private val callLogs: List<CustomCallLog>,
    private val onItemClick: (CustomCallLog) -> Unit
) : RecyclerView.Adapter<CallLogsAdapter.CallLogViewHolder>() {

    inner class CallLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val callLogName: TextView = itemView.findViewById(R.id.callLogName)
        private val callLogNumber: TextView = itemView.findViewById(R.id.callLogNumber)
        private val callLogDate: TextView = itemView.findViewById(R.id.callLogDate)
        private val callLogType: TextView = itemView.findViewById(R.id.callLogType)

        fun bind(callLog: CustomCallLog) {
            callLogName.text = callLog.name
            callLogNumber.text = callLog.number
            callLogDate.text = callLog.date
            callLogType.text = when (callLog.type) {
                CustomCallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                CustomCallLog.Calls.INCOMING_TYPE -> "Incoming"
                CustomCallLog.Calls.MISSED_TYPE -> "Missed"
                else -> "Unknown"
            }
            itemView.setOnClickListener { onItemClick(callLog) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_call_log, parent, false)
        return CallLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        holder.bind(callLogs[position])
    }

    override fun getItemCount(): Int = callLogs.size
}
