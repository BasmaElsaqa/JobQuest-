package com.example.jobquestbottomnavigation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        auth = FirebaseAuth.getInstance()

        // Initialize views
        var isPasswordVisible = false
        val toggle = findViewById<ImageView>(R.id.togglePasswordVisibility)
        val toggle2 = findViewById<ImageView>(R.id.toggleConfirmPasswordVisibility)
        val emailText = findViewById<EditText>(R.id.email_text)
        val passwordText = findViewById<EditText>(R.id.password_text)
        val confirmPasswordText = findViewById<EditText>(R.id.repassword)
        val signUpButton = findViewById<Button>(R.id.btn_signup)
        progress = findViewById(R.id.progress_bar)
        val alreadyUser: TextView = findViewById(R.id.already_user)

        toggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                passwordText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                toggle.setImageResource(R.drawable.ic_baseline_eye_24)
            } else {
                passwordText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                toggle.setImageResource(R.drawable.baseline_visibility_off_24)
            }
            // Move cursor to end of text
            passwordText.setSelection(passwordText.text.length)
        }
        toggle2.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                confirmPasswordText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                toggle.setImageResource(R.drawable.ic_baseline_eye_24)
            } else {
                confirmPasswordText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                toggle.setImageResource(R.drawable.baseline_visibility_off_24)
            }
            // Move cursor to end of text
            confirmPasswordText.setSelection(confirmPasswordText.text.length)
        }
        // Sign Up Button Click Listener
        signUpButton.setOnClickListener {
            val email = emailText.text.toString().trim()
            val password = passwordText.text.toString().trim()
            val confirmPassword = confirmPasswordText.text.toString().trim()

            // Validate inputs
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length <= 6) {
                Toast.makeText(this, "Password must be longer than 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show progress bar and disable button to prevent multiple clicks
            progress.visibility = View.VISIBLE
            signUpButton.isEnabled = false


            // Simulate network delay
            Handler(Looper.getMainLooper()).postDelayed({
                addUser(email, password)
            }, 3000)
            startActivity(Intent(this, AccountCreatedActivity::class.java))
            finish()
        }

        alreadyUser.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    // Create new user
    private fun addUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progress.isVisible = false

                if (task.isSuccessful) {
                    verifyEmail()
                } else {
                    Toast.makeText(this, task.exception?.message ?: "Error adding user", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Send verification email
    private fun verifyEmail() {
        val user = auth.currentUser

        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Check your email for verification", Toast.LENGTH_SHORT).show()
                    progress.visibility = View.GONE
                } else {
                    Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
