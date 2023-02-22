package com.luoyang.basemvp

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.luoyang.basemvp.utils.LogUtils

/**
 *
 *
 * @author luoyang
 * @date 2022/10/30
 */
class BaseLifecycleObserver :DefaultLifecycleObserver {
    companion object : LifecycleObserver {
        const val TAG = "BaseLifecycleObserver"
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        LogUtils.d(TAG, "onCreate $owner")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        LogUtils.d(TAG, "onStart $owner")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        LogUtils.d(TAG, "onResume $owner")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        LogUtils.d(TAG, "onPause $owner")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        LogUtils.d(TAG, "onStop $owner")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        LogUtils.d(TAG, "onDestroy $owner")
    }
}