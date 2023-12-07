package com.llj.commondemo.mavericks

import com.airbnb.mvrx.MavericksState

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 12-06-2023
 */
data class CounterData(val count: Int = 0) : MavericksState