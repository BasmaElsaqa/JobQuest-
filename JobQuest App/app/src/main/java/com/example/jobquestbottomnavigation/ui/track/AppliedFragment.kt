package com.example.jobquestbottomnavigation.ui.track

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.jobquestbottomnavigation.AppliedJob
import com.example.jobquestbottomnavigation.AppliedJobsAdapter
import com.example.jobquestbottomnavigation.EditProposalActivity
import com.example.jobquestbottomnavigation.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AppliedFragment : Fragment() {

    private lateinit var adapter: AppliedJobsAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_applied, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewAppliedJobs)

        // ✅ Correct use of lambda to handle click
        adapter = AppliedJobsAdapter { job ->
            val intent = Intent(requireContext(), EditProposalActivity::class.java)
            intent.putExtra("title", job.title)
            intent.putExtra("company", job.company)
            intent.putExtra("dateApplied", job.dateApplied)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return view
        }

        db.collection("users")
            .document(currentUser.uid)
            .collection("appliedJobs")
            .get()
            .addOnSuccessListener { result ->
                val jobs = result.documents.mapNotNull { doc ->
                    val title = doc.getString("title") ?: return@mapNotNull null
                    val company = doc.getString("company") ?: return@mapNotNull null
                    val dateApplied = doc.getString("dateApplied") ?: return@mapNotNull null
                    AppliedJob(title, company, dateApplied)
                }
                adapter.submitList(jobs)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load applied jobs", Toast.LENGTH_SHORT).show()
            }

        return view
    }
}
