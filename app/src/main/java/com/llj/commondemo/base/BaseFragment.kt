package com.llj.commondemo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-24-2023
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    lateinit var binding: VB

    abstract fun buildBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    abstract fun onViewCreate()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = buildBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreate()
    }

}