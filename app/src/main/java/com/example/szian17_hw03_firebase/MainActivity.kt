package com.example.szian17_hw03_firebase

import android.app.ActivityOptions
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var sendButton: Button
    private lateinit var usernameText: EditText
    private lateinit var emailText: EditText
    private lateinit var descriptionText: EditText
    private lateinit var sharedPref: SharedPreferences
    private lateinit var ref: DatabaseReference
    private lateinit var challengeReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = getPreferences(0)

        Log.i("SHAREDPREF", sharedPref.all.toString())

        sendButton = findViewById(R.id.sendButton)
        usernameText = findViewById(R.id.usernameText)
        emailText = findViewById(R.id.emailText)
        descriptionText = findViewById(R.id.descriptionText)

        if (sharedPref.contains("cache") == false) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
            with(sharedPref.edit()) {
                putBoolean("cache", true)
                apply()
            }
        }

        ref = FirebaseDatabase.getInstance().getReference("users").apply { keepSynced(true) }


        sendButton.setOnClickListener {
            sendData()
        }

        usernameText.setText(sharedPref.getString("username", ""))
        emailText.setText(sharedPref.getString("email", ""))
        descriptionText.setText(sharedPref.getString("description", ""))
    }

    private fun sendData() {

        val username = usernameText.text.toString().trim()
        val email = emailText.text.toString().trim()
        val desc = descriptionText.text.toString().trim()
        if (username.isEmpty()) {
            usernameText.error = "Please enter a username"
            return
        }

        if (!email.isEmailAddressValid()) {
            emailText.error = "Please enter a valid email address"
            return
        }

        val userId = ref.push().key
        if (userId != null) {
            sendButton.isEnabled = false
            val user = User(userId, username, email, desc)
            ref.child(userId).setValue(user).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("WRITE", "SUCCESS")
                } else {
                    Log.i("WRITE", "ERROR" + it.exception.toString())
                }
            }

            Toast.makeText(applicationContext, "User saved!", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, UserActivity::class.java)
            startActivity(intent)

            sendButton.isEnabled = true
        }

    }


    private fun String.isEmailAddressValid(): Boolean {
        return this.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    override fun onPause() {
        super.onPause()
        with(sharedPref.edit()) {
            putString("username", usernameText.text.toString())
            putString("email", emailText.text.toString())
            putString("description", descriptionText.text.toString())
            commit()
        }

    }
}
