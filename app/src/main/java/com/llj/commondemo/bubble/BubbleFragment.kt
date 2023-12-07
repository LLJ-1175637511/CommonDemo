package com.llj.commondemo.bubble

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.llj.commondemo.core.base.BaseFragment
import com.llj.commondemo.databinding.FragmentBubbleBinding

@SuppressLint("ClickableViewAccessibility")
class BubbleFragment : BaseFragment<FragmentBubbleBinding>() {

    private val bubbleCoverView by lazy {
        //气泡的遮罩层 点击全屏任意区域都使气泡消失
        ZxtBubbleView2(requireContext())
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

    private fun getWindowContentView() = requireActivity().window.decorView.findViewById<FrameLayout>(android.R.id.content)

    override fun buildBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentBubbleBinding {
        return FragmentBubbleBinding.inflate(inflater, container, false)
    }

    override fun onViewCreate() {
        showBubble()
    }

}