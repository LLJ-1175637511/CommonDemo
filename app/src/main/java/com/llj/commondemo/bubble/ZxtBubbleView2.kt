package com.llj.commondemo.bubble

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.llj.commondemo.R

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 08-04-2023
 */
class ZxtBubbleView2 : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val popUp: ImageView

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.zxt_study_bubble_layout, this, true)
        popUp = v.findViewById(R.id.bubbleUp)
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    private var dismissBubbleRunnable: Runnable? = null

    fun setPointAndShow(targetView: View, dismissBubbleRunnable: Runnable) {
        popUp.post {
            val targetRect = Rect()
            targetView.getGlobalVisibleRect(targetRect)
            val bubbleRect = Rect()
            getGlobalVisibleRect(bubbleRect)
            translationX = resources.displayMetrics.widthPixels - bubbleRect.width().toFloat() - dp2px(12 - 8)
            translationY = targetRect.bottom - bubbleRect.top.toFloat()
            val bubbleUpRect = Rect()
            popUp.getGlobalVisibleRect(bubbleUpRect)
            popUp.translationX = targetRect.centerX() - bubbleUpRect.centerX().toFloat()
        }
        this.dismissBubbleRunnable = dismissBubbleRunnable
        postDelayed(dismissBubbleRunnable, 8000)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dismissBubbleRunnable?.let { removeCallbacks(it) }
        dismissBubbleRunnable = null
    }

    private fun dp2px(pDp: Int): Float {
        val density = context.resources.displayMetrics.density
        return (pDp * density + 0.5f).toInt().toFloat()
    }
}