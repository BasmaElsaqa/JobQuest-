package com.example.jobquestbottomnavigation.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.jobquestbottomnavigation.R
import com.example.jobquestbottomnavigation.StatusPagerAdapter
import com.example.jobquestbottomnavigation.StatusPagerAdapter2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class ProfileFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.status_tabs)

        val adapter = StatusPagerAdapter2(this)
        viewPager.adapter = adapter

        val tabTitles = listOf("Reminders", "Notifications", "Settings")

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name") ?: "No Name"
                        val email = document.getString("email") ?: "No Email"

                        val nameTextView = view.findViewById<TextView>(R.id.name_tv)
                        val emailTextView = view.findViewById<TextView>(R.id.email_tv)

                        nameTextView.text = name
                        emailTextView.text = email
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
        }


        return view
    }
}
