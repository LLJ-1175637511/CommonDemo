package com.llj.commondemo.core.base

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {
    fun hideKeyboard(view: View?){
        view?.clearFocus()
        val manager = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
        manager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
