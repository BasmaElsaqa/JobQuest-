package com.example.jobquestbottomnavigation.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobquestbottomnavigation.Job
import com.example.jobquestbottomnavigation.R
import com.example.jobquestbottomnavigation.SavedAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SavedFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SavedAdapter
    private val savedJobs = mutableListOf<Job>()

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved, container, false)
        recyclerView = view.findViewById(R.id.recyclerSavedJobs)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = SavedAdapter(savedJobs) { job ->
            // Optional: Handle item click (e.g., navigate to job detail)
            Toast.makeText(requireContext(), "Clicked: ${job.title}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter

        fetchSavedJobs()

        return view
    }

    private fun fetchSavedJobs() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "Not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("users")
            .document(userId)
            .collection("savedJobs")
            .get()
            .addOnSuccessListener { result ->
                savedJobs.clear()
                for (doc in result) {
                    val job = doc.toObject(Job::class.java)
                    savedJobs.add(job)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load saved jobs", Toast.LENGTH_SHORT).show()
            }
    }

}
