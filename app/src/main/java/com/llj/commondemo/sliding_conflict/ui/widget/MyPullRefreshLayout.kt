package com.llj.commondemo.sliding_conflict.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.llj.commondemo.sliding_conflict.pullrefresh.PullRefreshLayout

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 08-09-2023
 */
class MyPullRefreshLayout : PullRefreshLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (isRefreshing) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = MeasureSpec.getSize(heightMeasureSpec) + refreshView.measuredHeight
            setMeasuredDimension(width, height)
            Log.d(TAG,"onMeasure isRefreshing cHeight:${MeasureSpec.getSize(heightMeasureSpec)} height:${height} ")
        }
        Log.d(TAG,"onMeasure super.onMeasure(widthMeasureSpec, heightMeasureSpec) ")
    }

    private val TAG = "MyPullRefreshLayout"
}