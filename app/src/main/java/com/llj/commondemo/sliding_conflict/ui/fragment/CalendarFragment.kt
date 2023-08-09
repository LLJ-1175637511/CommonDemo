package com.llj.commondemo.sliding_conflict.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.llj.commondemo.base.BaseFragment
import com.llj.commondemo.databinding.FragmentCalendarBinding
import com.llj.commondemo.databinding.FragmentTabViewpager2Binding
import com.llj.commondemo.sliding_conflict.ui.adapter.FragmentPagerAdapter

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 08-10-2023
 */
class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {

    override fun buildBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCalendarBinding {
        return FragmentCalendarBinding.inflate(inflater, container, false)
    }

    override fun onViewCreate() {

    }

}