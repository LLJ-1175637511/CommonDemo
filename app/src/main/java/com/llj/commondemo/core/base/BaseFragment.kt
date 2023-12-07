package com.llj.commondemo.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.llj.commondemo.core.launcher.FragmentLauncher
import com.llj.commondemo.core.structure.ZHIntent

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-24-2023
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment(), FragmentLauncher {

    lateinit var binding: VB

    abstract fun buildBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    abstract fun onViewCreate()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = buildBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreate()
    }

    override fun startFragment(zhIntent: ZHIntent) {
        val activity = requireActivity()
        if (activity is BaseFragmentActivity<*>) {
            activity.startFragment(zhIntent)
        }
    }

    open fun getMainActivity(): BaseFragmentActivity<*> {
        return if (requireActivity() is BaseFragmentActivity<*>) {
            requireActivity() as BaseFragmentActivity<*>
        } else {
            val name = if (activity == null) null else requireActivity().javaClass.simpleName
            throw IllegalStateException("Must be added to BaseFragmentActivity: Current is $name")
        }
    }

    override fun startFragmentForResult(zhIntent: ZHIntent, target: Fragment?, requestCode: Int) {

    }

}