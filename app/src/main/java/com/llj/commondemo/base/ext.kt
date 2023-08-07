package com.llj.commondemo.base

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-24-2023
 */

fun AppCompatActivity.startNewActivity(clazz: Class<*>) {
    startActivity(Intent(this, clazz))
}

fun Int.dp(): Int {
    val density = BaseApplication.get().resources.displayMetrics.density
    return (this * density + 0.5f).toInt()
}