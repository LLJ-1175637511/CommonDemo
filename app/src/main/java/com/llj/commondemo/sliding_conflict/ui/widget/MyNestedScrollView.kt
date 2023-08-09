package com.llj.commondemo.sliding_conflict.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.llj.commondemo.R
import com.llj.commondemo.sliding_conflict.pullrefresh.PullRefreshLayout

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 08-03-2023
 */
class MyNestedScrollView : NestedScrollLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val TAG = "NestedScrollDemo"
    private val TAG2 = "pullRefreshLayout"

    private lateinit var headerView: View
    private lateinit var stickHeaderView: View
    private lateinit var pullRefreshLayout: PullRefreshLayout
    private var mNestedScrollingChild: View? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        headerView = (getChildAt(0) as ViewGroup).getChildAt(0)
        stickHeaderView = findViewById(R.id.stickHeaderView)
        pullRefreshLayout = findRefreshLayout(getChildAt(0) as ViewGroup) ?: throw Exception("not find pullRefreshLayout")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 调整contentView的高度为父容器高度，使之填充布局，避免父容器滚动后出现空白
        val lp: ViewGroup.LayoutParams = pullRefreshLayout.layoutParams
        lp.height = measuredHeight + stickHeaderView.measuredHeight
        pullRefreshLayout.layoutParams = lp
        Log.d(TAG2, "onMeasure measuredHeight:${measuredHeight} lp.height:${lp.height}")
    }

//    override fun measureChild(child: View, parentWidthMeasureSpec: Int, parentHeightMeasureSpec: Int) {
//        if (child == pullRefreshLayout && pullRefreshLayout.isRefreshing) {
//            val lp = child.layoutParams
//            val childWidthSpec = getChildMeasureSpec(parentWidthMeasureSpec, paddingLeft + paddingRight, lp.width)
//            val childHeightSpec = getChildMeasureSpec(parentHeightMeasureSpec, paddingTop + paddingBottom, lp.height)
//            val height = MeasureSpec.getSize(childHeightSpec) + pullRefreshLayout.refreshView.measuredHeight
//            val newChildHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(childHeightSpec))
//            child.measure(childWidthSpec, newChildHeightSpec)
//            return
//        }
//        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec)
//    }

    private fun appendRefreshHeight() = if (pullRefreshLayout.isRefreshing) pullRefreshLayout.refreshView.measuredHeight else 0

    override fun onStartNestedScroll(child: View, target: View, axes: Int): Boolean {
        val superOnStartNestedScroll = super.onStartNestedScroll(child, target, axes)
        Log.d(TAG, "parent onStartNestedScroll isVertical:${axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0} superOnStartNestedScroll:${superOnStartNestedScroll}")
        return superOnStartNestedScroll
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        val superOnStartNestedScroll = super.onStartNestedScroll(child, target, axes, type)
        Log.d(TAG, "parent onStartNestedScroll superOnStartNestedScroll:${superOnStartNestedScroll} target:${target} child:${child}")
        return superOnStartNestedScroll
    }

//    override fun onNestedScrollAccepted(child: View, target: View, nestedScrollAxes: Int) {
//        super.onNestedScrollAccepted(child, target, nestedScrollAxes)
//        Log.d(TAG, "parent onNestedScrollAccepted nestedScrollAxes:${nestedScrollAxes}")
//    }
//
//    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
//        super.onNestedScrollAccepted(child, target, axes, type)
//        Log.d(TAG, "parent onNestedScrollAccepted axes:${axes} type:${type}")
//    }

//    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
//        super.onNestedPreScroll(target, dx, dy, consumed)
//        Log.d(TAG, "parent onNestedPreScroll dx:" + dx + " dy:" + dy + " consumed:" + consumed.contentToString())
//    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(target, dx, dy, consumed, type)
        Log.d(TAG, "parent onNestedPreScroll dx:" + dx + " dy:" + dy + " consumed:" + consumed.contentToString())
        // 向上滑动。若当前topview可见，需要将topview滑动至不可见
        val hideTop = dy > 0 && hideHeaderAndRefreshView()
        Log.d(TAG2, "parent onNestedPreScroll scrollY:${scrollY} refreshViewHeight:${pullRefreshLayout.refreshView?.measuredHeight} headerHeight:${headerView.measuredHeight} stickView.top:${stickHeaderView.top}")
        if (hideTop) {
            scrollBy(0, dy)
            consumed[1] = dy
        }
    }

    private fun hideHeaderAndRefreshView() = scrollY < headerView.measuredHeight + appendRefreshHeight() + stickHeaderView.measuredHeight

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
//        if (dyConsumed == 0 && dyUnconsumed > 0 && pullRefreshLayout.isRefreshing && (target is NestedScrollView)) {
//            mNestedScrollingChild?.let {
//                val rv = findRecyclerView((it as ViewGroup))
//                if (rv != null && rv.canScrollVertically(-1)) {
//                    rv.scrollBy(0, dyUnconsumed)
//                    Log.d(TAG, "onNestedScroll rv:${rv.canScrollVertically(-1)} dyUnconsumed:${dyUnconsumed}")
//                }
//            }
//        }
        Log.d(TAG, "parent onNestedScroll dyConsumed:" + dyConsumed + " dyUnconsumed:" + dyUnconsumed + " consumed:" + consumed.contentToString())
        Log.d(TAG, "parent onNestedScroll scrollY:$scrollY measuredHeight:$measuredHeight mNestedScrollingChild:${mNestedScrollingChild.toString()} ${mNestedScrollingChild?.canScrollVertically(-1)}")
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

    private fun findRefreshLayout(viewGroup: ViewGroup): PullRefreshLayout? {
        for (i in 0 until viewGroup.childCount) {
            val view = viewGroup.getChildAt(i)
            if (view is PullRefreshLayout) {
                return view
            } else if (viewGroup.getChildAt(i) is ViewGroup) {
                val childPullRefreshLayout = findRefreshLayout(viewGroup.getChildAt(i) as ViewGroup)
                if (childPullRefreshLayout != null) {
                    return childPullRefreshLayout
                }
            }
            continue
        }
        return null
    }

    private fun findRecyclerView(viewGroup: ViewGroup): RecyclerView? {
        for (i in 0 until viewGroup.childCount) {
            val view = viewGroup.getChildAt(i)
            if (view is RecyclerView) {
                return view
            } else if (viewGroup.getChildAt(i) is ViewGroup) {
                val childPullRefreshLayout = findRecyclerView(viewGroup.getChildAt(i) as ViewGroup)
                if (childPullRefreshLayout != null) {
                    return childPullRefreshLayout
                }
            }
            continue
        }
        return null
    }

}