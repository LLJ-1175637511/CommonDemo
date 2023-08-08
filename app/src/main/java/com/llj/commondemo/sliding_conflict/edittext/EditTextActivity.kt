package com.llj.commondemo.sliding_conflict.edittext

import android.annotation.SuppressLint
import android.util.Log
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.llj.commondemo.base.BaseActivity
import com.llj.commondemo.base.startNewActivity
import com.llj.commondemo.databinding.ActivityBubbleBinding
import com.llj.commondemo.databinding.ActivityEdittextBinding
import com.llj.commondemo.databinding.ActivityMainBinding
import com.llj.commondemo.sliding_conflict.SlidingConflictActivity
import com.llj.commondemo.sliding_conflict.SlidingConflictActivity2
import org.libpag.PAGFile

@SuppressLint("ClickableViewAccessibility")
class EditTextActivity : BaseActivity<ActivityEdittextBinding>() {

    override fun buildBinding(): ActivityEdittextBinding = ActivityEdittextBinding.inflate(layoutInflater)

    private var newIndex = 0

    override fun onCreate() {
        binding.next.setOnClickListener {
            binding.edittext.setSelection(++newIndex)
        }
        binding.front.setOnClickListener {
            binding.edittext.setSelection(--newIndex)
        }
        binding.edittext.setOnFocusChangeListener { v, hasFocus ->
            Log.d("EditTextActivity","hasFocus:${hasFocus}")
            if (hasFocus && !binding.edittext.text.isNullOrBlank()) {
                Toast.makeText(this, "focus", Toast.LENGTH_SHORT).show()
            }
        }
        binding.hideKeyboard.setOnClickListener {
            binding.edittext.clearFocus()
        }

    }

}