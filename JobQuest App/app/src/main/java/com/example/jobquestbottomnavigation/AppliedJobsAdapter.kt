package com.example.jobquestbottomnavigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppliedJobsAdapter(
    private val onEditClick: (AppliedJob) -> Unit
) : RecyclerView.Adapter<AppliedJobsAdapter.ViewHolder>() {

    private var jobList = listOf<AppliedJob>()

    fun submitList(list: List<AppliedJob>) {
        jobList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.title)
        private val companyView: TextView = itemView.findViewById(R.id.company)
        private val dateView: TextView = itemView.findViewById(R.id.date_applied)
        private val editBtn: View = itemView.findViewById(R.id.edit_proposal_btn)

        fun bind(job: AppliedJob) {
            titleView.text = job.title
            companyView.text = job.company
            dateView.text = job.dateApplied

            editBtn.setOnClickListener {
                onEditClick(job)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.applied_jobs_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(jobList[position])
    }

    override fun getItemCount() = jobList.size
}
