package com.example.jobquestbottomnavigation.ui.jobs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobquestbottomnavigation.Job
import com.example.jobquestbottomnavigation.JobDetailsActivity
import com.example.jobquestbottomnavigation.JobsAdapter
import com.example.jobquestbottomnavigation.R
import com.example.jobquestbottomnavigation.databinding.FragmentJobsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class JobsFragment : Fragment() {

    private lateinit var binding: FragmentJobsBinding
    private lateinit var jobList: ArrayList<Job>
    private lateinit var adapter: JobsAdapter
    private lateinit var firestore: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobsBinding.inflate(inflater, container, false)

        // Back arrow click
        binding.trackPageHeader.findViewById<View>(R.id.back_arrow).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


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

        fetchJobsFromFirestore()

        return binding.root
    }

    private fun fetchJobsFromFirestore() {
        firestore = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Fetch saved jobs first
        val savedJobsMap = mutableSetOf<String>()

        if (userId != null) {
            firestore.collection("users").document(userId).collection("savedJobs")
                .get().addOnSuccessListener { savedResult ->
                    for (doc in savedResult) {
                        val savedJob = doc.toObject(Job::class.java)
                        savedJobsMap.add("${savedJob.title}_${savedJob.company}")
                    }

                    // Fetch all jobs
                    firestore.collection("jobs").get()
                        .addOnSuccessListener { result ->
                            jobList.clear()
                            for (document in result) {
                                val job = document.toObject(Job::class.java)
                                val key = "${job.title}_${job.company}"
                                job.isSaved = savedJobsMap.contains(key)
                                jobList.add(job)
                            }
                            adapter.notifyDataSetChanged()
                        }
                }
        }
    }

}
