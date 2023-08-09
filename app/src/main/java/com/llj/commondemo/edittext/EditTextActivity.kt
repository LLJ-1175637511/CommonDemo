package com.llj.commondemo.edittext

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.llj.commondemo.base.BaseActivity
import com.llj.commondemo.databinding.ActivityEdittextBinding

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