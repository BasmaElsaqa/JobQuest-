package com.example.jobquestbottomnavigation.ui.track

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.jobquestbottomnavigation.R
import com.example.jobquestbottomnavigation.StatusPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class TrackFragment : Fragment() {

        private lateinit var viewPager: ViewPager2
        private lateinit var tabLayout: TabLayout

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_track, container, false)

            viewPager = view.findViewById(R.id.view_pager)
            tabLayout = view.findViewById(R.id.status_tabs)

            val adapter = StatusPagerAdapter(this)
            viewPager.adapter = adapter

            val tabTitles = listOf("Applied", "Interviewing", "Rejected", "Offer")

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()

            return view
        }
    }
