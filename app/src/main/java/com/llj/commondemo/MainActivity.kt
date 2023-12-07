package com.llj.commondemo

import android.annotation.SuppressLint
import android.os.Bundle
import com.llj.commondemo.core.base.BaseBindingActivity
import com.llj.commondemo.core.base.BaseFragmentActivity
import com.llj.commondemo.databinding.ActivityMainBinding

@SuppressLint("ClickableViewAccessibility")
class MainActivity : BaseFragmentActivity<ActivityMainBinding>() {

    override fun buildBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    private val mainFragment by lazy { MainFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addMainContainerFragment()
    }

    private fun addMainContainerFragment() {
        supportFragmentManager.beginTransaction().add(binding.mainFragmentContainer.id, mainFragment, mainFragment.tag).commit()
    }

}