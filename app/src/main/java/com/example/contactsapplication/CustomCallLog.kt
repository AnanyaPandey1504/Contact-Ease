package com.example.contactsapplication

data class CustomCallLog(
    val name: String,
    val number: String,
    val date: String,
    val type: Int,
    val time: String,
    val callType: Int
)
{
    object Calls {
        const val OUTGOING_TYPE = 2
        const val INCOMING_TYPE = 1
        const val MISSED_TYPE = 3
    }
}
