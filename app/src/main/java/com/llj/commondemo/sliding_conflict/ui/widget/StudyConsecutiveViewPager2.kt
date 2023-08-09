package com.llj.commondemo.sliding_conflict.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.donkingliang.consecutivescroller.ConsecutiveViewPager2
import com.llj.commondemo.sliding_conflict.pullrefresh.PullRefreshLayout
import java.lang.IllegalArgumentException

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-03-2023
 */
class StudyConsecutiveViewPager2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConsecutiveViewPager2(context, attrs, defStyleAttr) {

    override fun getCurrentScrollerView(): View {
        mRecyclerView.layoutManager?.findViewByPosition(currentItem)?.let { itemView ->
            return getScrollView(itemView) ?: super.getCurrentScrollerView()
        }
        return super.getCurrentScrollerView()
    }

    private fun getScrollView(view: View): View? {
        if (view is RecyclerView || view is NestedScrollView) return view
        if (view is ViewGroup) {
            if (view.childCount > 0) {
                //如果是RefreshLayout里面有两个child
                if (view is SwipeRefreshLayout || view is PullRefreshLayout){
                    view.children.find { it is RecyclerView }?.let { return it }
                }
                return getScrollView(view.getChildAt(0))
            }
        }
        throw IllegalArgumentException("current view is not include recyclerView or ViewGroup")
    }

}