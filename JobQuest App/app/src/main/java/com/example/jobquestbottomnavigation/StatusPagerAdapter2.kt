package com.example.jobquestbottomnavigation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jobquestbottomnavigation.ui.profile.NotificationsFragment
import com.example.jobquestbottomnavigation.ui.profile.RemindersFragment
import com.example.jobquestbottomnavigation.ui.profile.SettingsFragment

class StatusPagerAdapter2(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RemindersFragment()
            1 -> NotificationsFragment()
            2 -> SettingsFragment()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }
}