package com.llj.commondemo.core.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.llj.commondemo.core.launcher.FragmentLauncher

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 12-06-2023
 */
abstract class BaseBindingActivity<VB : ViewBinding> : BaseActivity(), IViewBinding<VB> {

    protected val binding by lazy { buildBinding() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

}