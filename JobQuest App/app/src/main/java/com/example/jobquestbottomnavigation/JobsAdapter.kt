package com.example.jobquestbottomnavigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JobsAdapter(
    private val jobList: List<Job>,
    private val onItemClick: (Job) -> Unit
) : RecyclerView.Adapter<JobsAdapter.JobViewHolder>() {

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val company: TextView = itemView.findViewById(R.id.company)
        val salary: TextView = itemView.findViewById(R.id.salary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.jobs_item_list, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobList[position]
        holder.title.text = job.title
        holder.company.text = "${job.company} • ${job.location}"
        holder.salary.text = job.salary

        holder.itemView.setOnClickListener {
            onItemClick(job)
        }
    }

    override fun getItemCount(): Int = jobList.size
}
