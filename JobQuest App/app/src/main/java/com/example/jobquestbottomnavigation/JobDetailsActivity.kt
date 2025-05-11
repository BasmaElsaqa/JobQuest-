package com.example.jobquestbottomnavigation

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import com.google.android.material.button.MaterialButton
import android.widget.ImageView


class JobDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details)

        // ✅ Hide the ActionBar and go fullscreen
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


        val title = intent.getStringExtra("title") ?: ""
        val company = intent.getStringExtra("company") ?: ""
        val location = intent.getStringExtra("location") ?: ""
        val salary = intent.getStringExtra("salary") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val requirements = intent.getStringExtra("requirements") ?: ""
        val benefits = intent.getStringExtra("benefits") ?: ""

        findViewById<TextView>(R.id.job_title).text = title
        findViewById<TextView>(R.id.company_name).text = "$company • $location"

        findViewById<TextView>(R.id.job_description).text = description
        findViewById<TextView>(R.id.job_requirements).text = requirements
        findViewById<TextView>(R.id.job_benefits).text = benefits


        val salaryTag = findViewById<TextView>(R.id.salary_tag)
        salaryTag.text = salary

        val locationTag=findViewById<TextView>(R.id.location_tag)
        locationTag.text=location

        // 🚀 Navigate to ApplyForPositionActivity
        val applyNowButton = findViewById<Button>(R.id.btn_apply_now)
        applyNowButton.setOnClickListener {
            val intent = Intent(this, ApplyForPositionActivity::class.java)

            // Pass data using extras
            intent.putExtra("title", title)
            intent.putExtra("company", company)
            intent.putExtra("location", location)
            intent.putExtra("salary", salary)

            startActivity(intent)
        }

        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        backArrow.setOnClickListener {
            finish() // Goes back to the previous activity
        }


    }
}

