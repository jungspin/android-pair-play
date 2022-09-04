package com.pinslog.pairplay.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pinslog.pairplay.view.NonPairedFragment
import com.pinslog.pairplay.view.PairedFragment

private const val NUM_TABS = 2
class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
          0 -> {return NonPairedFragment()}
          1 -> {return PairedFragment()}
        }
        return PairedFragment()
    }
}