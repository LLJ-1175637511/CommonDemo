package com.llj.commondemo

import android.annotation.SuppressLint
import com.llj.commondemo.base.BaseActivity
import com.llj.commondemo.base.startNewActivity
import com.llj.commondemo.databinding.ActivityMainBinding
import com.llj.commondemo.sliding_conflict.SlidingConflictActivity
import com.llj.commondemo.sliding_conflict.SlidingConflictActivity2
import com.llj.commondemo.sliding_conflict.bubble.BubbleActivity

@SuppressLint("ClickableViewAccessibility")
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun buildBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate() {
        binding.slidingConflict.setOnClickListener {
            startNewActivity(SlidingConflictActivity::class.java)
        }
        binding.slidingConflict2.setOnClickListener {
            startNewActivity(SlidingConflictActivity2::class.java)
        }
        binding.bubbleActivity.setOnClickListener {
            startNewActivity(BubbleActivity::class.java)
        }
    }

}