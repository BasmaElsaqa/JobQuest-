// ReminderAdaptor

package com.example.jobquestbottomnavigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobquestbottomnavigation.R
import com.example.jobquestbottomnavigation.Reminder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class RemindersAdapter(private val remindersList: MutableList<Reminder>) :
    RecyclerView.Adapter<RemindersAdapter.ReminderViewHolder>() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = remindersList[position]
        holder.bind(reminder)

        // Handle delete click
        holder.deleteIcon.setOnClickListener {
            userId?.let {
                db.collection("users").document(it)
                    .collection("reminders")
                    .whereEqualTo("title", reminder.title)
                    .whereEqualTo("dueDate", reminder.dueDate)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            db.collection("users").document(it)
                                .collection("reminders")
                                .document(document.id)
                                .delete()
                        }

                        remindersList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, remindersList.size)
                    }
            }
        }
    }

    override fun getItemCount(): Int = remindersList.size

    class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.reminderTitle)
        private val date: TextView = itemView.findViewById(R.id.reminderDate)
        val deleteIcon: ImageView = itemView.findViewById(R.id.deleteIcon)

        fun bind(reminder: Reminder) {
            title.text = reminder.title
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            date.text = formatter.format(Date(reminder.dueDate))
        }
    }
}