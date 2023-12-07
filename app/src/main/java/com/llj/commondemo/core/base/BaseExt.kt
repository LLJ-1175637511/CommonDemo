package com.llj.commondemo.core.base

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.llj.commondemo.app.BaseApplication

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-24-2023
 */

fun FragmentActivity.startNewActivity(clazz: Class<*>) {
    startActivity(Intent(this, clazz))
}

fun Fragment.startNewActivity(clazz: Class<*>) {
    requireActivity().startNewActivity(clazz)
}

fun Int.dp(): Int {
    val density = BaseApplication.get().resources.displayMetrics.density
    return (this * density + 0.5f).toInt()
}