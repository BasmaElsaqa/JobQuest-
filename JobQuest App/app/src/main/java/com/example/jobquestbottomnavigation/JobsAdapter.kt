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

class JobsAdapter(
    private val jobList: List<Job>,
    private val onItemClick: (Job) -> Unit
) : RecyclerView.Adapter<JobsAdapter.JobViewHolder>() {

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val company: TextView = itemView.findViewById(R.id.company)
        val salary: TextView = itemView.findViewById(R.id.salary)
        val location: TextView = itemView.findViewById(R.id.location)
        val saveButton: ImageView = itemView.findViewById(R.id.saveButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.jobs_item_list, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobList[position]

        holder.title.text = job.title
        holder.company.text = job.company
        holder.salary.text = job.salary
        holder.location.text = job.location

        // Set correct icon based on isSaved
        if (job.isSaved) {
            holder.saveButton.setImageResource(R.drawable.saved_button)
        } else {
            holder.saveButton.setImageResource(R.drawable.save_icon_white_inside)
        }

        holder.itemView.setOnClickListener {
            onItemClick(job)
        }

        holder.saveButton.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val db = FirebaseFirestore.getInstance()

            if (userId != null) {
                val jobDoc = db.collection("users")
                    .document(userId)
                    .collection("savedJobs")
                    .document("${job.title}_${job.company}")

                if (job.isSaved) {
                    // Already saved → remove from saved
                    jobDoc.delete().addOnSuccessListener {
                        job.isSaved = false
                        notifyItemChanged(position)
                        Toast.makeText(holder.itemView.context, "Removed from saved", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Not saved → save it
                    jobDoc.set(job).addOnSuccessListener {
                        job.isSaved = true
                        notifyItemChanged(position)
                        Toast.makeText(holder.itemView.context, "Saved", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(holder.itemView.context, "Please log in to save jobs.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = jobList.size

    fun updateJob(updatedJob: Job) {
        val index = jobList.indexOfFirst { it.title == updatedJob.title && it.company == updatedJob.company }
        if (index != -1) {
            (jobList as MutableList)[index] = updatedJob
            notifyItemChanged(index)
        }
    }
}
