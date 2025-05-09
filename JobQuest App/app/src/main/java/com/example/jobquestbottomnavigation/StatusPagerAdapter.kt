package com.example.jobquestbottomnavigation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jobquestbottomnavigation.ui.track.AppliedFragment
import com.example.jobquestbottomnavigation.ui.track.InterviewingFragment
import com.example.jobquestbottomnavigation.ui.track.OfferFragment
import com.example.jobquestbottomnavigation.ui.track.RejectedFragment

class StatusPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AppliedFragment()
            1 -> InterviewingFragment()
            2 -> RejectedFragment()
            3 -> OfferFragment()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }
}
