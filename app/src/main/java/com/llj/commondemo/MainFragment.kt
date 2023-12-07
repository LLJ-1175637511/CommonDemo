package com.llj.commondemo

import android.view.LayoutInflater
import android.view.ViewGroup
import com.llj.commondemo.core.base.BaseFragment
import com.llj.commondemo.bubble.BubbleFragment
import com.llj.commondemo.core.structure.ZHIntent
import com.llj.commondemo.databinding.FragmentMainBinding
import com.llj.commondemo.mavericks.CounterFragment

class MainFragment : BaseFragment<FragmentMainBinding>() {

    override fun buildBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun onViewCreate() {
        binding.bubble.setOnClickListener {
            startFragment(ZHIntent(BubbleFragment::class.java))
        }
        binding.counter.setOnClickListener {
            startFragment(ZHIntent(CounterFragment::class.java))
        }
    }

}