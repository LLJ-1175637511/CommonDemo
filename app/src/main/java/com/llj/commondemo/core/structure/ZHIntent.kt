package com.llj.commondemo.core.structure

import android.os.Bundle
import java.io.Serializable

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 12-06-2023
 */

data class ZHIntent(
    val clazz: Class<*>,
    val arguments: Bundle? = null,
    val className: String = clazz.name
) : Serializable