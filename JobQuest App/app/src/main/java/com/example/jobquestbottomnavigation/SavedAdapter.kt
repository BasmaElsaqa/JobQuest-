package com.example.jobquestbottomnavigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SavedAdapter(
    private val savedJobs: MutableList<Job>,
    private val onItemClick: (Job) -> Unit
) : RecyclerView.Adapter<SavedAdapter.SavedViewHolder>() {

    inner class SavedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val company: TextView = itemView.findViewById(R.id.company)
        val salary: TextView = itemView.findViewById(R.id.salary)
        val saveButton: ImageView = itemView.findViewById(R.id.saveButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.jobs_item_list, parent, false)
        return SavedViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {
        val job = savedJobs[position]

        holder.title.text = job.title
        holder.company.text = "${job.company} • ${job.location}"
        holder.salary.text = job.salary

        // Set saved icon
        holder.saveButton.setImageResource(R.drawable.saved_button)

        // Click to view details
        holder.itemView.setOnClickListener {
            onItemClick(job)
        }

        // Click to remove from saved list
        holder.saveButton.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                val db = FirebaseFirestore.getInstance()
                val jobDoc = db.collection("users")
                    .document(userId)
                    .collection("savedJobs")
                    .document("${job.title}_${job.company}")

                jobDoc.delete().addOnSuccessListener {
                    // Remove from local list and update UI
                    savedJobs.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    Toast.makeText(holder.itemView.context, "Removed from saved", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(holder.itemView.context, "Failed to remove job", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int = savedJobs.size
}
