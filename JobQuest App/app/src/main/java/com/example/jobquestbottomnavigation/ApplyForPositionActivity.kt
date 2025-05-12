package com.example.jobquestbottomnavigation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ApplyForPositionActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_apply_for_position)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val title = intent.getStringExtra("title") ?: ""
        val company = intent.getStringExtra("company") ?: ""

        val jobTitleTextView = findViewById<TextView>(R.id.position_title)
        val jobCompanyTextView = findViewById<TextView>(R.id.position_company)

        jobTitleTextView.text = title
        jobCompanyTextView.text = company

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.submit_btn).setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                Toast.makeText(this, "Please log in first.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newJob = hashMapOf(
                "title" to title,
                "company" to company,
                "dateApplied" to getCurrentDate()
            )

            db.collection("users")
                .document(currentUser.uid)
                .collection("appliedJobs")
                .add(newJob)
                .addOnSuccessListener {
                    startActivity(Intent(this, ApplicationSubmittedActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to submit application.", Toast.LENGTH_SHORT).show()
                }
        }

        findViewById<MaterialButton>(R.id.cancel_btn).setOnClickListener { finish() }
        findViewById<ImageView>(R.id.back_arrow).setOnClickListener { finish() }
    }

    private fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return formatter.format(Date())
    }
}
