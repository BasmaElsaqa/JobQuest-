package com.example.jobquestbottomnavigation.ui.jobs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobquestbottomnavigation.Job
import com.example.jobquestbottomnavigation.JobDetailsActivity
import com.example.jobquestbottomnavigation.JobsAdapter
import com.example.jobquestbottomnavigation.databinding.FragmentJobsBinding
import com.google.firebase.database.*

class JobsFragment : Fragment() {

    private lateinit var binding: FragmentJobsBinding
    private lateinit var jobList: ArrayList<Job>
    private lateinit var adapter: JobsAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobsBinding.inflate(inflater, container, false)

        jobList = ArrayList()
        adapter = JobsAdapter(jobList) { selectedJob ->
            val intent = Intent(requireContext(), JobDetailsActivity::class.java).apply {
                putExtra("title", selectedJob.title)
                putExtra("company", selectedJob.company)
                putExtra("salary", selectedJob.salary)
                putExtra("location", selectedJob.location)
                putExtra("description", selectedJob.description)
                putExtra("requirements", selectedJob.requirements)
                putExtra("benefits", selectedJob.benefits)
            }
            startActivity(intent)
        }

        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter

        fetchJobsFromFirebase()

        return binding.root
    }

    private fun fetchJobsFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Jobs")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                jobList.clear()
                for (jobSnapshot in snapshot.children) {
                    val job = jobSnapshot.getValue(Job::class.java)
                    job?.let { jobList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Log or handle the error if needed
            }
        })
    }
}
