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
import com.google.android.material.button.MaterialButton


class ApplyForPositionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_apply_for_position)

        val title = intent.getStringExtra("title") ?: ""
        val company = intent.getStringExtra("company") ?: ""
        val location = intent.getStringExtra("location") ?: ""
        val salary = intent.getStringExtra("salary") ?: ""


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

        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        backArrow.setOnClickListener {
            startActivity(Intent(this, JobDetailsActivity::class.java))
            finish()
        }
        val cancelBtn = findViewById<MaterialButton>(R.id.cancel_btn)
        cancelBtn.setOnClickListener {
            startActivity(Intent(this, JobDetailsActivity::class.java))
            finish()
        }


        val submitApplicationButton = findViewById<Button>(R.id.submit_btn)
        submitApplicationButton.setOnClickListener {
            startActivity(Intent(this, ApplicationSubmittedActivity::class.java))
            finish()
        }

    }
}