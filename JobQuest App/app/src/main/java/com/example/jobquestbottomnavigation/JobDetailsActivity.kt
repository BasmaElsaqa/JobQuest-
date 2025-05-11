package com.example.jobquestbottomnavigation

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class JobDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details)

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

        val salaryTag = TextView(this).apply {
            text = salary
            setPadding(12, 6, 12, 6)
            setBackgroundColor(Color.parseColor("#E5E7EB"))
            setTextColor(Color.BLACK)
            textSize = 14f
        }

        findViewById<LinearLayout>(R.id.tags_container).addView(salaryTag)
    }
}

