package com.llj.commondemo.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-24-2023
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    lateinit var binding: VB

    abstract fun buildBinding(): VB

    abstract fun onCreate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = buildBinding()
        setContentView(binding.root)
        onCreate()
    }

}