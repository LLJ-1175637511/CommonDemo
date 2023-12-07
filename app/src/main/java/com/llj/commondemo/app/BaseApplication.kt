package com.llj.commondemo.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.airbnb.mvrx.Mavericks
import com.llj.commondemo.core.log.Logger

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 12-07-2023
 */
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setApplication(this)
        Mavericks.initialize(this)
        registerActivityLifecycleCallbacks(object : DefaultActivityLifecycleCallbacks() {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Logger.d("ActivityLifecycle: onActivityCreated $activity")
            }
        })
    }

    companion object{
        private var app: Application? = null

        fun get() = app ?: throw Exception("MainApplication not init")

        private fun setApplication(application: Application) {
            app = application
        }
    }
}