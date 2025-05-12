package com.example.jobquestbottomnavigation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.button.MaterialButton


class EditProposalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_proposal)



        val title = intent.getStringExtra("title") ?: ""
        val company = intent.getStringExtra("company") ?: ""
        val location = intent.getStringExtra("location") ?: ""
        val salary = intent.getStringExtra("salary") ?: ""
        val dateApplied = intent.getStringExtra("dateApplied")


        val jobTitleTextView = findViewById<TextView>(R.id.position_title)
        val jobCompanyTextView = findViewById<TextView>(R.id.position_company)
        val jobLocationTextView = findViewById<TextView>(R.id.position_location)
        val jobSalaryTextView = findViewById<TextView>(R.id.position_salary)

        jobTitleTextView.text = title
        jobCompanyTextView.text = company
        jobLocationTextView.text = location
        jobSalaryTextView.text = salary

        // ✅ Hide the ActionBar and go fullscreen
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Back arrow click navigates back
        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        backArrow.setOnClickListener {
            finish()  // Navigates back to previous fragment/activity
        }

        // Cancel button click navigates back
        val cancelButton = findViewById<MaterialButton>(R.id.cancel_btn)
        cancelButton.setOnClickListener {
            finish()  // Navigates back
        }

        // Save button click shows toast and navigates back
        val saveButton = findViewById<MaterialButton>(R.id.save_btn)
        saveButton.setOnClickListener {
            Toast.makeText(this, "Your proposal is updated successfully", Toast.LENGTH_SHORT).show()
            finish()  // Navigates back
        }


    }
}