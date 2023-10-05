package com.example.bangkitnavigationapi.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity, private val username : String) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        val fragment = GithubUserConnectionFragment()
        val bundle = Bundle()

        bundle.putString(GithubUserConnectionFragment.USERNAME, username)

        when (position) {
            0 -> bundle.putString("action", GithubUserConnectionFragment.GET_FOLLOWERS)
            1 -> bundle.putString("action", GithubUserConnectionFragment.GET_FOLLOWING)
        }

        fragment.arguments = bundle
        return fragment
    }

    override fun getItemCount(): Int = 2
}