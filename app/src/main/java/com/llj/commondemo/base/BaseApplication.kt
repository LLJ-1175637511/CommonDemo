package com.llj.commondemo.base

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import java.lang.Exception

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 08-16-2023
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        set(this)
    }

    companion object {
        private var app: Application? = null

        fun get() = app ?: throw Exception("BaseApplication not init")

        private fun set(application: Application) {
            app = application
            app?.registerActivityLifecycleCallbacks(object :ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

                }

                override fun onActivityStarted(activity: Activity) {

                }

                override fun onActivityResumed(activity: Activity) {
                    Log.d("BaseApplication","onActivityResumed activity:${activity}")
                }

                override fun onActivityPaused(activity: Activity) {

                }

                override fun onActivityStopped(activity: Activity) {

                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

                }

                override fun onActivityDestroyed(activity: Activity) {

                }

                override fun onActivityPostResumed(activity: Activity) {
                    super.onActivityPostResumed(activity)
                    Log.d("BaseApplication","onActivityPostResumed activity:${activity}")
                }

            })
        }
    }
}