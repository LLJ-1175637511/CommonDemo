package com.llj.commondemo.core.base

import androidx.viewbinding.ViewBinding

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 12-07-2023
 */
interface IViewBinding<VB : ViewBinding> {

    fun buildBinding(): VB

}