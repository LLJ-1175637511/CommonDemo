package com.llj.commondemo.sliding_conflict.ui.activity

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.llj.commondemo.base.BaseActivity
import com.llj.commondemo.databinding.ActivitySlidingConflictBinding
import com.llj.commondemo.sliding_conflict.ui.adapter.FragmentPagerAdapter
import com.llj.commondemo.sliding_conflict.ui.fragment.CalendarFragment
import com.llj.commondemo.sliding_conflict.ui.fragment.TabFragment


/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-24-2023
 */
class SlidingConflictActivity : BaseActivity<ActivitySlidingConflictBinding>() {

    override fun buildBinding(): ActivitySlidingConflictBinding = ActivitySlidingConflictBinding.inflate(layoutInflater)

    override fun onCreate() {
        binding.close.setOnClickListener {
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.swipeRefreshLayout.setOnRefreshListener {

        }
        supportFragmentManager.beginTransaction().add(binding.calendar.id, CalendarFragment(), "CalendarFragment").commitNowAllowingStateLoss()
        val adapter = FragmentPagerAdapter(this, getFragments())
        binding.viewPager2.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position -> tab.text = "Tab" + (position + 1) }.attach()
    }

    private fun getFragments(): List<Fragment> {
        val fragmentList: MutableList<TabFragment> = java.util.ArrayList()
        fragmentList.add(TabFragment())
        fragmentList.add(TabFragment())
        return fragmentList
    }

}