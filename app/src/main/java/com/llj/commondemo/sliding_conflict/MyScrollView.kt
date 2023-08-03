package com.llj.commondemo.sliding_conflict

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-24-2023
 */
@SuppressLint("ViewConstructor")
class MyScrollView: NestedScrollLayout {

    private var mLastY = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val TAG = "NestedScrollLayout"

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        super.onInterceptTouchEvent(ev)
        //默认不拦截
        var intercepted = false
        val y = ev.y.toInt()
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
            }

            MotionEvent.ACTION_MOVE -> {
                val detY = y - mLastY
                val firstView = getChildAt(0)
                // 还要自己找子View
                if (firstView !is ViewGroup || firstView.childCount == 0) return false
                val childView = firstView.getChildAt(1) ?: return false //拦截
                //根据手势判断子scrollView是否在顶部或底部
                val isChildScrolledTop = detY > 0 && !childView.canScrollVertically(-1)
                val isChildScrolledBottom = detY < 0 && !childView.canScrollVertically(1)
                Log.d(TAG,"detY > 0:${detY > 0} childView.canScrollVertically(-1):${childView.canScrollVertically(-1)} detY < 0:${detY < 0} childView.canScrollVertically(1):${childView.canScrollVertically(1)} isStartFling:${isStartFling}")
//                Log.d(TAG,"isChildScrolledTop:${isChildScrolledTop} isChildScrolledBottom:${isChildScrolledBottom}")
//                intercepted = isChildScrolledTop || isChildScrolledBottom
                intercepted = isChildScrolledBottom
            }

            MotionEvent.ACTION_UP -> intercepted = false
        }
        mLastY = y
        return intercepted
    }

}