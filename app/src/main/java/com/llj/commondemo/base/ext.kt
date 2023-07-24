package com.llj.commondemo.base

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-24-2023
 */

fun AppCompatActivity.startNewActivity(clazz: Class<*>) {
    startActivity(Intent(this, clazz))
}