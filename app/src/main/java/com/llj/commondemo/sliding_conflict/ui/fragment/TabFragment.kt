package com.llj.commondemo.sliding_conflict.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.llj.commondemo.base.BaseFragment
import com.llj.commondemo.databinding.FragmentTabViewpager2Binding
import com.llj.commondemo.sliding_conflict.ui.adapter.FragmentPagerAdapter

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 08-10-2023
 */
class TabFragment : BaseFragment<FragmentTabViewpager2Binding>() {

    override fun buildBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTabViewpager2Binding {
        return FragmentTabViewpager2Binding.inflate(inflater, container, false)
    }

    override fun onViewCreate() {
        val adapter = FragmentPagerAdapter(requireActivity(), getFragments())
        binding.viewPager2.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position -> tab.text = "Tab" + (position + 1) }.attach()
    }

    private fun getFragments(): List<Fragment> {
        val fragmentList: MutableList<ListFragment> = java.util.ArrayList()
        fragmentList.add(ListFragment())
        fragmentList.add(ListFragment())
        fragmentList.add(ListFragment())
        fragmentList.add(ListFragment())
        return fragmentList
    }

}