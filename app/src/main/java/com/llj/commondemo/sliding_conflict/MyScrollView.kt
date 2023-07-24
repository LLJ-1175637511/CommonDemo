package com.llj.commondemo.sliding_conflict

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-24-2023
 */
@SuppressLint("ViewConstructor")
class MyScrollView: NestedScrollView {

    private lateinit var topView: View
    private lateinit var contentView: View

    private var mLastY = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        //默认不拦截
        var intercepted = false
        val y = ev.y.toInt()
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
                super.onInterceptTouchEvent(ev)
            }

            MotionEvent.ACTION_MOVE -> {
                val detY = y - mLastY
                val firstView = getChildAt(0)
                // 还要自己找子View
                if (firstView !is ViewGroup || firstView.childCount == 0) return true
                val childView = firstView.getChildAt(1) ?: return true //拦截
                //根据手势判断子scrollView是否在顶部或底部
                val isChildScrolledTop = detY > 0 && !childView.canScrollVertically(-1)
                val isChildScrolledBottom = detY < 0 && !childView.canScrollVertically(1)
                intercepted = isChildScrolledTop || isChildScrolledBottom
            }

            MotionEvent.ACTION_UP -> intercepted = false
        }
        mLastY = y
        return intercepted
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        topView = (getChildAt(0) as ViewGroup).getChildAt(0)
        contentView = (getChildAt(0) as ViewGroup).getChildAt(1) as ViewGroup
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 调整contentView的高度为父容器高度，使之填充布局，避免父容器滚动后出现空白
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val lp: ViewGroup.LayoutParams = contentView.layoutParams
        lp.height = measuredHeight
        contentView.setLayoutParams(lp)
    }

}