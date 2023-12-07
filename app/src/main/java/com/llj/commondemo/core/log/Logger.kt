package com.llj.commondemo.core.log

import android.util.Log

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 12-07-2023
 */
object Logger {

    const val TAG = "LLJDemo"

    fun d(msg: String) {
        Log.d(TAG, msg)
    }

    fun d(tag: String, msg: String) {
        Log.d(TAG, msg)
    }

}