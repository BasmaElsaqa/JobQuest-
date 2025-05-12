package com.example.jobquestbottomnavigation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobquestbottomnavigation.NotificationsAdapter
import com.example.jobquestbottomnavigation.R
import com.example.jobquestbottomnavigation.Reminder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotificationsFragment : Fragment() {

    private lateinit var notificationsRecyclerView: RecyclerView
    private lateinit var notificationsAdapter: NotificationsAdapter
    private val notificationsList = mutableListOf<Reminder>()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        notificationsRecyclerView = view.findViewById(R.id.notifications_list)

        notificationsAdapter = NotificationsAdapter(notificationsList)
        notificationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        notificationsRecyclerView.adapter = notificationsAdapter

        loadNotifications()

        return view
    }

    private fun loadNotifications() {
        val userId = auth.currentUser?.uid ?: return

        // Load reminders stored in Firestore
        db.collection("users").document(userId)
            .collection("reminders")
            .whereLessThanOrEqualTo("dueDate", System.currentTimeMillis())
            .orderBy("dueDate")
            .get()
            .addOnSuccessListener { documents ->
                notificationsList.clear()
                for (document in documents) {
                    val reminder = document.toObject(Reminder::class.java)
                    notificationsList.add(reminder)
                }
                notificationsAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load notifications", Toast.LENGTH_SHORT).show()
            }
    }
}

