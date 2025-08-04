package com.example.chefchat.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.chefchat.ArchiveFragment
import com.example.chefchat.LikedRecipesFragment


class ProfilePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ArchiveFragment()
            1 -> LikedRecipesFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
