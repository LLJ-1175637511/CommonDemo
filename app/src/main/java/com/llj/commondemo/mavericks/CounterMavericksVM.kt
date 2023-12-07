package com.llj.commondemo.mavericks

import com.airbnb.mvrx.MavericksViewModel

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 12-06-2023
 */
class CounterMavericksVM(initialState: CounterData) : MavericksViewModel<CounterData>(initialState) {

    fun add(){
        setState { copy(count = count + 1) }
    }

    fun minus(){
        setState { copy(count = count - 1) }
    }

}