package com.dombikpanda.doktarasor.ViewPagerAdapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dombikpanda.doktarasor.view.activity.HomeActivity

class ViewPagerAdapter(
    val items:ArrayList<Fragment>,
    activity: HomeActivity
):FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }
}