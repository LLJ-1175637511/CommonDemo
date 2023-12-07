package com.llj.commondemo.core.base

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.llj.commondemo.core.launcher.FragmentLauncher
import com.llj.commondemo.core.router.RouterConst
import com.llj.commondemo.core.router.RouterUtils
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
        val intent = Intent(this, RouterUtils.getComponentActivity(zhIntent.clazz))
        intent.putExtra(RouterConst.EXTRA_ZHINTENT, zhIntent)
        startActivity(intent)
    }

}