package com.llj.commondemo.sliding_conflict.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.llj.commondemo.base.BaseFragment
import com.llj.commondemo.databinding.FragmentListBinding
import com.llj.commondemo.sliding_conflict.ui.adapter.ItemAdapter

/**
 * @Author donkingliang
 * @Description
 * @Date 2020/4/18
 */
class ListFragment : BaseFragment<FragmentListBinding>() {

    override fun buildBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentListBinding.inflate(inflater, container, false)

    override fun onViewCreate() {
        binding.refreshLayout.isEnabled = false
        initList()
    }

    private val adapter by lazy {
        val list = ArrayList<String>()
        repeat(21) {
            list.add("数据${it}")
        }
        ItemAdapter(list)
    }

    private fun initList() {
        binding.recyclerView.adapter = adapter
    }
}