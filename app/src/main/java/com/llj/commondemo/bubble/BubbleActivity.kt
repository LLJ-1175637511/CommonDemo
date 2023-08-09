package com.llj.commondemo.bubble

import android.annotation.SuppressLint
import android.widget.FrameLayout
import com.llj.commondemo.base.BaseActivity
import com.llj.commondemo.databinding.ActivityBubbleBinding

@SuppressLint("ClickableViewAccessibility")
class BubbleActivity : BaseActivity<ActivityBubbleBinding>() {

    override fun buildBinding(): ActivityBubbleBinding = ActivityBubbleBinding.inflate(layoutInflater)

    private val bubbleCoverView by lazy {
        //气泡的遮罩层 点击全屏任意区域都使气泡消失
        ZxtBubbleView2(this)
    }

    override fun onCreate() {
        showBubble()
    }

    private fun showBubble() {
        //如果展示气泡 添加遮罩层 点击任意区域取消气泡
        getWindowContentView().addView(bubbleCoverView)
        bubbleCoverView.setPointAndShow(binding.courseRemind, Runnable {
            dismissBubble()
        })
        getWindowContentView().setOnTouchListener { _, _ ->
            dismissBubble()
            false
        }
    }

    private fun dismissBubble(){
        getWindowContentView().setOnTouchListener(null)
        getWindowContentView().removeView(bubbleCoverView)
    }

    private fun getWindowContentView() = window.decorView.findViewById<FrameLayout>(android.R.id.content)

}