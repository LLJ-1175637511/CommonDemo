package com.llj.commondemo.base

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
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
        if (isFullScreen()) transparentStatusBar(window)
        super.onCreate(savedInstanceState)
        binding = buildBinding()
        setContentView(binding.root)
        onCreate()
    }

    open fun isFullScreen() = false

    private fun transparentStatusBar(window: Window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility =
            systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.decorView.systemUiVisibility = systemUiVisibility
        window.statusBarColor = Color.TRANSPARENT

        //设置状态栏文字颜色
//        setStatusBarTextColor(window, AppCompatDelegate.NightMode.isNightMode(window.context))
    }

}