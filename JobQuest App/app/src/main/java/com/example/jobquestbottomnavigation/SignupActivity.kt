package com.example.jobquestbottomnavigation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.jobquestbottomnavigation.AccountCreatedActivity
import com.example.jobquestbottomnavigation.LoginActivity
import com.example.jobquestbottomnavigation.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var progress: ProgressBar
    private lateinit var nameInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        var isPasswordVisible = false
        val toggle = findViewById<ImageView>(R.id.togglePasswordVisibility)
        val toggle2 = findViewById<ImageView>(R.id.toggleConfirmPasswordVisibility)
        val emailText = findViewById<EditText>(R.id.email_text)
        val passwordText = findViewById<EditText>(R.id.password_text)
        val confirmPasswordText = findViewById<EditText>(R.id.repassword)
        val signUpButton = findViewById<Button>(R.id.btn_signup)
        nameInput = findViewById(R.id.name_text)
        progress = findViewById(R.id.progress_bar)
        val alreadyUser: TextView = findViewById(R.id.already_user)

        toggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            passwordText.inputType = if (isPasswordVisible) {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            toggle.setImageResource(if (isPasswordVisible) R.drawable.ic_baseline_eye_24 else R.drawable.baseline_visibility_off_24)
            passwordText.setSelection(passwordText.text.length)
        }

        toggle2.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            confirmPasswordText.inputType = if (isPasswordVisible) {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            toggle.setImageResource(if (isPasswordVisible) R.drawable.ic_baseline_eye_24 else R.drawable.baseline_visibility_off_24)
            confirmPasswordText.setSelection(confirmPasswordText.text.length)
        }

        signUpButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailText.text.toString().trim()
            val password = passwordText.text.toString().trim()
            val confirmPassword = confirmPasswordText.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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

            progress.visibility = View.VISIBLE
            signUpButton.isEnabled = false

            Handler(Looper.getMainLooper()).postDelayed({
                addUser(name, email, password)
            }, 3000)
        }

        alreadyUser.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun addUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progress.isVisible = false

                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val userMap = mapOf(
                            "name" to name,
                            "email" to email
                        )
                        database.child("users").child(userId).setValue(userMap)
                    }

                    verifyEmail()
                    startActivity(Intent(this, AccountCreatedActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, task.exception?.message ?: "Error adding user", Toast.LENGTH_SHORT).show()
                }
            }
    }

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
