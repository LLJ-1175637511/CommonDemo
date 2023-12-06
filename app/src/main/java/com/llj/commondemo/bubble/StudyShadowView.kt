package com.llj.commondemo.bubble

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.llj.commondemo.R


/**
 * @author liulinjie @ Zhihu Inc.
 * @since 08-04-2023
 */
class StudyShadowView : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val shadowPaint = Paint()

    init {
        setWillNotDraw(false)
        shadowPaint.color = ContextCompat.getColor(context,android.R.color.transparent)
        shadowPaint.setShadowLayer(dp2px(8), 0f, dp2px(2), ContextCompat.getColor(context, R.color.zxt_study_bubble_shadow_color))
    }

    private val contentRadius = dp2px(8)

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(0f, contentRadius, width.toFloat(), height.toFloat(), contentRadius, contentRadius, shadowPaint)
    }

    private fun dp2px(pDp: Int): Float {
        val density = context.resources.displayMetrics.density
        return (pDp * density + 0.5f).toInt().toFloat()
    }
}