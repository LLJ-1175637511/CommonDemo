package com.llj.commondemo.core.host

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import com.llj.commondemo.core.base.BaseFragmentActivity
import com.llj.commondemo.core.router.RouterConst
import com.llj.commondemo.core.router.RouterUtils
import com.llj.commondemo.core.structure.ZHIntent
import com.llj.commondemo.databinding.ActivityHostBinding

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 12-06-2023
 */
open class HostActivity : BaseFragmentActivity<ActivityHostBinding>() {

    override fun buildBinding(): ActivityHostBinding {
        return ActivityHostBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val zhIntent = parseZHIntent(intent)
        if (zhIntent == null) {
            finish()
            return
        }

        super.onCreate(savedInstanceState)

        val newFragment = supportFragmentManager.fragmentFactory.instantiate(ClassLoader.getSystemClassLoader(), zhIntent.className)
        newFragment.arguments = zhIntent.arguments
        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, newFragment, HOST_BOTTOM_FRAGMENT_TAG)
            .commitAllowingStateLoss()
    }

    protected open fun parseZHIntent(intent: Intent): ZHIntent? {
        var zhIntent: ZHIntent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_ZHINTENT, ZHIntent::class.java)
        } else {
            intent.getSerializableExtra(EXTRA_ZHINTENT) as? ZHIntent
        }
        if (zhIntent == null) {
            if (!TextUtils.isEmpty(intent.getStringExtra(EXTRA_URL))) {
                zhIntent = RouterUtils.resolve(intent.getStringExtra(EXTRA_URL))
            }
        }
        return zhIntent
    }

    companion object {
        const val EXTRA_ZHINTENT: String = RouterConst.EXTRA_ZHINTENT
        const val EXTRA_URL: String = RouterConst.EXTRA_URL
        const val HOST_BOTTOM_FRAGMENT_TAG = "HostActivity::ParentFragment"
    }

}