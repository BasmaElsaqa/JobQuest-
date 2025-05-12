package com.example.jobquestbottomnavigation.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.jobquestbottomnavigation.LoginActivity
import com.example.jobquestbottomnavigation.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SettingsFragment : Fragment() {

    private lateinit var logoutTextView: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser ?: return view

        logoutTextView = view.findViewById(R.id.logout_tv)

        logoutTextView.setOnClickListener {
            auth.signOut()
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Navigate back to login screen
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val changePasswordTextView = view.findViewById<TextView>(R.id.change_password_tv)
        changePasswordTextView.setOnClickListener {
            showPasswordPrompt { enteredPassword ->
                reAuthenticateUser(enteredPassword) { success ->
                    if (success) {
                        showNewPasswordPrompt { newPassword ->
                            currentUser.updatePassword(newPassword)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(requireContext(), "Password changed successfully", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(requireContext(), "Password change failed", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(requireContext(), "Re-authentication failed. Please log in again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



        val deleteAccountTextView = view.findViewById<TextView>(R.id.delete_account_tv)
        deleteAccountTextView.setOnClickListener {
            showPasswordPrompt { enteredPassword ->
                reAuthenticateUser(enteredPassword) { success ->
                    if (success) {
                        currentUser.delete()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(requireContext(), "Account deleted", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(requireContext(), LoginActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(requireContext(), "Account deletion failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(requireContext(), "Re-authentication failed. Please log in again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        return view
    }

    // Re-authenticate the user with their email and password
    private fun reAuthenticateUser(password: String, callback: (Boolean) -> Unit) {
        val user = auth.currentUser
        val email = user?.email

        if (email != null && password.isNotBlank()) {
            val credential = EmailAuthProvider.getCredential(email, password)
            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful)
                }
        } else {
            callback(false)
        }
    }

    private fun showPasswordPrompt(onPasswordEntered: (String) -> Unit) {
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Password")
            .setMessage("Enter your current password:")
            .setView(input)
            .setPositiveButton("Confirm") { _, _ ->
                val password = input.text.toString()
                onPasswordEntered(password)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNewPasswordPrompt(onNewPasswordEntered: (String) -> Unit) {
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        AlertDialog.Builder(requireContext())
            .setTitle("New Password")
            .setMessage("Enter your new password:")
            .setView(input)
            .setPositiveButton("Set") { _, _ ->
                val newPassword = input.text.toString()
                onNewPasswordEntered(newPassword)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


}
