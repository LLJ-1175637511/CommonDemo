package com.llj.commondemo.app

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 12-07-2023
 */
open class DefaultActivityLifecycleCallbacks:Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}