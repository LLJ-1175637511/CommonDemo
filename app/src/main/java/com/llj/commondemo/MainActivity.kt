package com.llj.commondemo

import android.annotation.SuppressLint
import com.llj.commondemo.base.BaseActivity
import com.llj.commondemo.base.startNewActivity
import com.llj.commondemo.databinding.ActivityMainBinding
import com.llj.commondemo.sliding_conflict.ui.activity.SlidingConflictActivity
import com.llj.commondemo.bubble.BubbleActivity
import com.llj.commondemo.edittext.EditTextActivity

@SuppressLint("ClickableViewAccessibility")
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun buildBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate() {
        binding.slidingConflict.setOnClickListener {
            startNewActivity(SlidingConflictActivity::class.java)
        }
        binding.bubbleActivity.setOnClickListener {
            startNewActivity(BubbleActivity::class.java)
        }
        binding.edittextActivity.setOnClickListener {
            startNewActivity(EditTextActivity::class.java)
        }
    }

}