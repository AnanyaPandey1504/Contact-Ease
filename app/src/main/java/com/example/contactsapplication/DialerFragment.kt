package com.example.contactsapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.Manifest
import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

class DialerFragment : Fragment() {
    private lateinit var numberInput: EditText
    private lateinit var callButton: Button
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dialer, container, false)
        numberInput = view.findViewById(R.id.numberInput)
        val callButton: AppCompatImageView = view.findViewById(R.id.callButton)

        val buttons = listOf(
            view.findViewById<Button>(R.id.button1),
            view.findViewById<Button>(R.id.button2),
            view.findViewById<Button>(R.id.button3),
            view.findViewById<Button>(R.id.button4),
            view.findViewById<Button>(R.id.button5),
            view.findViewById<Button>(R.id.button6),
            view.findViewById<Button>(R.id.button7),
            view.findViewById<Button>(R.id.button8),
            view.findViewById<Button>(R.id.button9),
            view.findViewById<Button>(R.id.btn_2),
            view.findViewById<Button>(R.id.btn_3)
        )
        buttons.forEach { button ->
            button.setOnClickListener {
                numberInput.append(button.text)
            }
        }

        view.findViewById<ImageView>(R.id.btn_1)
        callButton.setOnClickListener {
            val phoneNumber = numberInput.text.toString()
            if (phoneNumber.isNotEmpty()) {
                makeCall(phoneNumber)
            } else {
                Toast.makeText(requireContext(), "Please enter a number", Toast.LENGTH_SHORT).show()
            }
        }
        return view
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
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            makeCall(numberInput.text.toString())
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}