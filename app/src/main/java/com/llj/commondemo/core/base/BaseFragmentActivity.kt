package com.llj.commondemo.core.base

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.llj.commondemo.core.host.HostActivity
import com.llj.commondemo.core.launcher.FragmentLauncher
import com.llj.commondemo.core.router.RouterConst
import com.llj.commondemo.core.structure.ZHIntent

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 12-07-2023
 */
abstract class BaseFragmentActivity<VB : ViewBinding> : BaseBindingActivity<VB>(), FragmentLauncher {

    override fun onStop() {
        super.onStop()
        if (currentFocus != null) {
            KeyboardUtils.hideKeyboard(currentFocus)
        }
    }

    override fun startFragment(zhIntent: ZHIntent) {
        startFragmentForResult(zhIntent, null, -1)
    }

    override fun startFragmentForResult(zhIntent: ZHIntent, target: Fragment?, requestCode: Int) {
        val intent = Intent(this, HostActivity::class.java)
        intent.putExtra(RouterConst.EXTRA_ZHINTENT, zhIntent)
        startActivity(intent)
    }
}