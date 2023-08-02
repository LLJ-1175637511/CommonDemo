package com.zhihu.zhixuetang.android.study.calendar.pullrefresh


/**
 * @author liyue @ Zhihu Inc.
 * @since 05-08-2023
 */

fun isPagResource(assetName: String?): Boolean {
    return assetName?.endsWith(".pag") ?: false
}