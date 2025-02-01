package com.example.contactsapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.Manifest

class ContactsFragment : Fragment() {
    private lateinit var contactsRecyclerView: RecyclerView
    private lateinit var contactsAdapter: ContactsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)
        contactsRecyclerView = view.findViewById(R.id.contactsRecyclerView)
        contactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val contacts = fetchContacts()
        contactsAdapter = ContactsAdapter(contacts) { contact ->
            showCallDialog(contact.number)
        }
        contactsRecyclerView.adapter = contactsAdapter

        return view
    }
    @SuppressLint("Range")
    private fun fetchContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contacts.add(Contact(name, number))
            }
        }
        return contacts.distinctBy { it.number }
    }
    private fun showCallDialog(phoneNumber: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Call")
            .setMessage("Do you want to call $phoneNumber?")
            .setPositiveButton("Call") { _, _ ->
                makeCall(phoneNumber)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startActivity(intent)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 101)
        }
    }
}