package com.llj.commondemo.sliding_conflict

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.llj.commondemo.sliding_conflict.pullrefresh.PullRefreshLayout

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 08-03-2023
 */
class MyPullRefreshLayout : PullRefreshLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var refreshCanScrollListener: ICanScrollListener? = null

    private var lastY = 0f
    private var scrollY = 0f

//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        when(ev.action){
//            MotionEvent.ACTION_DOWN -> {
//                lastY = ev.y
//            }
//            MotionEvent.ACTION_MOVE -> {
//                val dy = ev.y
//                val isTop = refreshCanScrollListener?.isTop() ?: false
//                scrollY += dy
//                if (scrollY >= 0 && dy >= 0){
//                    return true
//                }
//            }
//            MotionEvent.ACTION_UP ->{
//                scrollY = 0f
//            }
//        }
//        return super.onInterceptTouchEvent(ev)
//    }

//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        when(ev.action){
//            MotionEvent.ACTION_DOWN -> {
//                lastY = ev.y
//            }
//            MotionEvent.ACTION_MOVE -> {
//                val dy = ev.y
//                val isTop = refreshCanScrollListener?.isTop() ?: false
//                scrollY += dy
//                lastY = ev.y
//                if (scrollY >= 0 && dy >= 0){
//                    return true
//                }
//            }
//            MotionEvent.ACTION_UP ->{
//                scrollY = 0f
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top + 120, right, bottom + 120)
    }

    interface ICanScrollListener {
        fun isTop(): Boolean
    }

    fun addRefreshCanScrollListener(listener: ICanScrollListener){
        refreshCanScrollListener = listener
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        refreshCanScrollListener = null
    }
}