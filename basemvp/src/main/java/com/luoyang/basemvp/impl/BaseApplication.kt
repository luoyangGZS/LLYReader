package com.luoyang.basemvp.impl

import android.app.Application
import com.luoyang.basemvp.utils.LogUtils

/**
 * 基础 application
 * todo 每个应用都有实现他，否则打印日志失败
 * @author luoyang
 * @date 2022/10/28
 */
abstract class BaseApplication : Application() {

    companion object {
        lateinit var instance: BaseApplication
    }

    /**
     * 调用顺序：主级函数 > init > 次级函数
     */
    init {
        instance = this
    }


    override fun onCreate() {
        super.onCreate()
        init()
        LogUtils.clearLogFiles()
    }

    /**
     * Application初始化工作放在此处
     */
    abstract fun init()
}