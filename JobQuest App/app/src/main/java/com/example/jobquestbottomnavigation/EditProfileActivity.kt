package com.example.jobquestbottomnavigation

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var headlineEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // ✅ Hide the ActionBar and go fullscreen
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


        nameEditText = findViewById(R.id.edit_name)
        headlineEditText = findViewById(R.id.edit_headline)
        emailEditText = findViewById(R.id.edit_email)
        saveButton = findViewById(R.id.btn_save_profile)

        val userId = auth.currentUser?.uid

        if (userId != null) {
            // Load existing data
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        nameEditText.setText(document.getString("name"))
                        headlineEditText.setText(document.getString("headline"))
                        emailEditText.setText(document.getString("email"))
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
                }

            // Save updated info
            saveButton.setOnClickListener {
                val name = nameEditText.text.toString().trim()
                val headline = headlineEditText.text.toString().trim()
                val email = emailEditText.text.toString().trim()

                val updates = hashMapOf(
                    "name" to name,
                    "headline" to headline,
                    "email" to email
                )

                db.collection("users").document(userId)
                    .update(updates as Map<String, Any>)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                        finish() // go back to profile screen
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                    }
            }
        } else {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show()
        }

        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        backArrow.setOnClickListener {
            finish() // Goes back to the previous activity
        }
    }
}
