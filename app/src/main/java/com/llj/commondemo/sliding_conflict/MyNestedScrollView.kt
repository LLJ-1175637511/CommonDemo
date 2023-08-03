package com.llj.commondemo.sliding_conflict

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 08-03-2023
 */
class MyNestedScrollView: NestedScrollView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val TAG = "NestedScrollDemo"

    private lateinit var contentView: ViewGroup

    override fun onFinishInflate() {
        super.onFinishInflate()
        contentView = (getChildAt(0) as ViewGroup).getChildAt(1) as ViewGroup
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 调整contentView的高度为父容器高度，使之填充布局，避免父容器滚动后出现空白
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val lp: ViewGroup.LayoutParams = contentView.getLayoutParams()
        lp.height = measuredHeight
        contentView.layoutParams = lp
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int): Boolean {
        val superOnStartNestedScroll = super.onStartNestedScroll(child, target, axes)
        Log.d(TAG, "parent onStartNestedScroll isVertical:${axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0} superOnStartNestedScroll:${superOnStartNestedScroll}")
        return superOnStartNestedScroll
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        val superOnStartNestedScroll = super.onStartNestedScroll(child, target, axes, type)
        Log.d(TAG, "parent onStartNestedScroll isVertical:${axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0} superOnStartNestedScroll:${superOnStartNestedScroll}")
        return superOnStartNestedScroll
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        super.onNestedPreScroll(target, dx, dy, consumed)
        Log.d(TAG, "parent onNestedPreScroll dx:" + dx + " dy:" + dy + " consumed:" + consumed.contentToString())
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(target, dx, dy, consumed, type)
        Log.d(TAG, "parent onNestedPreScroll dx:" + dx + " dy:" + dy + " consumed:" + consumed.contentToString())
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        Log.d(TAG, "parent onNestedScroll dyConsumed:$dyConsumed dyUnconsumed:$dyUnconsumed")
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        Log.d(TAG, "parent onNestedScroll dyConsumed:$dyConsumed dyUnconsumed:$dyUnconsumed")
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int, consumed: IntArray) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)
        Log.d(TAG, "parent onNestedScroll dyConsumed:" + dyConsumed + " dyUnconsumed:" + dyUnconsumed + " consumed:" + consumed.contentToString())
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        val start = super.startNestedScroll(axes, type)
        Log.d(TAG, "parent startNestedScroll axes:${axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0} type:${type}")
        return start
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?, type: Int): Boolean {
        val superDispatchNestedPreScroll = super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
        Log.d(TAG, "parent dispatchNestedPreScroll superDispatchNestedPreScroll:${superDispatchNestedPreScroll} dx:" + dx + " dy:" + dy + " consumed:" + consumed.contentToString())
        return superDispatchNestedPreScroll
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?, type: Int, consumed: IntArray) {
        super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed)
        Log.d(TAG, "parent dispatchNestedScroll dyConsumed:" + dyConsumed + " dyUnconsumed:" + dyUnconsumed + " consumed:" + consumed.contentToString())
    }



}