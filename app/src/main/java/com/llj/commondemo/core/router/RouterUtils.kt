package com.llj.commondemo.core.router

import android.app.Activity
import com.llj.commondemo.core.host.HostActivity
import com.llj.commondemo.core.structure.ZHIntent

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 12-07-2023
 */
object RouterUtils {

    private val defaultActivity by lazy { HostActivity::class.java }

    fun resolve(url: String?): ZHIntent? {
        return null
    }

    /**
     * 多 Activity 模式下，返回解析结果对应的 Activity
     */
    fun getComponentActivity(target: Class<*>): Class<out Activity> {
        if (target.isAnnotationPresent(AttachedActivity::class.java)) {
            return target.getAnnotation(AttachedActivity::class.java)!!.value.java
        }
        return defaultActivity
    }

}