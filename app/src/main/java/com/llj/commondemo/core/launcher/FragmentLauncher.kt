package com.llj.commondemo.core.launcher

import androidx.fragment.app.Fragment
import com.llj.commondemo.core.structure.ZHIntent

/**
 * Fragment 启动器，用于启动 Fragment，实现需要是 Activity
 */
interface FragmentLauncher {
    /**
     * 启动 Fragment
     */
    fun startFragment(zhIntent: ZHIntent)

    /**
     * 启动 Fragment
     */
    fun startFragmentForResult(zhIntent: ZHIntent, target: Fragment?, requestCode: Int)
}