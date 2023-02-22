package com.luoyang.basemvp.utils

import android.annotation.SuppressLint
import android.os.Environment
import android.util.Log
import com.luoyang.basemvp.impl.BaseApplication
import java.text.SimpleDateFormat
import java.util.*

/**
 * 打印log
 *
 * @author luoyang
 * @date 2022/10/28
 */
object LogUtils {

    /**
     *  全局log开关
     */
    private const val LOG_GLOBAL_SWITCH: Boolean = true

    /**
     *  日志写入文件开关
     */
    private const val LOG_WRITE_SWITCH: Boolean = true

    /**
     *  开放局部log等级开关
     */
    private const val LOG_PART_SWITCH: Char = 'd'

    private const val DEFAULT_TAG: String = "ly_library"

    private const val LOG_FILE_POSTFIX = ".log"

    /**
     * 清理日志文件-10天前的
     */
    private const val CLEAR_LOG_FILE_BEFORE = 9

    fun w(text: String) {
        log(DEFAULT_TAG, text, 'w')
    }

    fun e(text: String) {
        log(DEFAULT_TAG, text, 'e')
    }

    fun d(text: String) {
        log(DEFAULT_TAG, text, 'd')
    }

    fun i(text: String) {
        log(DEFAULT_TAG, text, 'i')
    }

    fun v(text: String) {
        log(DEFAULT_TAG, text, 'v')
    }

    fun w(tag: String, msg: Any) {
        log(tag, msg.toString(), 'w')
    }

    fun e(tag: String, msg: Any) {
        log(tag, msg.toString(), 'e')
    }

    fun d(tag: String, msg: Any) {
        log(tag, msg.toString(), 'd')
    }

    fun i(tag: String, msg: Any) {
        log(tag, msg.toString(), 'i')
    }

    fun v(tag: String, msg: Any) {
        log(tag, msg.toString(), 'v')
    }

    fun w(tag: String, text: String) {
        log(tag, text, 'w')
    }

    fun e(tag: String, text: String) {
        log(tag, text, 'e')
    }

    fun d(tag: String, text: String) {
        log(tag, text, 'd')
    }

    fun i(tag: String, text: String) {
        log(tag, text, 'i')
    }

    fun v(tag: String, text: String) {
        log(tag, text, 'v')
    }

    /**9
     * 输出日志
     */
    private fun log(tag: String, msg: String, level: Char) {
        if (LOG_GLOBAL_SWITCH) {
            if ('e' == level && ('e' == LOG_PART_SWITCH || 'v' == LOG_PART_SWITCH)) {
                Log.e(tag, msg)
            } else if ('w' == level && ('w' == LOG_PART_SWITCH || 'v' == LOG_PART_SWITCH)) {
                Log.w(tag, msg)
            } else if ('d' == level && ('d' == LOG_PART_SWITCH || 'v' == LOG_PART_SWITCH)) {
                Log.d(tag, msg)
            } else if ('i' == level && ('d' == LOG_PART_SWITCH || 'v' == LOG_PART_SWITCH)) {
                Log.i(tag, msg)
            } else {
                Log.v(tag, msg)
            }
            if (LOG_WRITE_SWITCH) {
                write2File("$level", tag, msg)
            }
        }

    }

    /**
     * 格式-文件名称
     */
    @SuppressLint("SimpleDateFormat")
    private val FORMAT_LOG_FILE_NAME = SimpleDateFormat("yyyy-MM-dd")

    /**
     * 格式-日志内容
     */
    @SuppressLint("SimpleDateFormat")
    private val FORMAT_LOG_DATA = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    /**
     * 日志写入文件
     */
    private fun write2File(logType: String, tag: String, text: String) {
        val date = Date()
        val logName = "${FORMAT_LOG_FILE_NAME.format(date)}$LOG_FILE_POSTFIX"
        val data = "${FORMAT_LOG_DATA.format(date)}  $logType  $tag  $text"

        FileStorageUtils.saveDataToStoragePrivateFilesDir(
            data,
            Environment.DIRECTORY_DCIM,
            logName,
            BaseApplication.instance
        )
    }

    fun clearLogFiles() {
        d("LogUtils", "clearLogFiles older than $CLEAR_LOG_FILE_BEFORE days")
        val file = BaseApplication.instance.getExternalFilesDir(Environment.DIRECTORY_DCIM)
        FileStorageUtils.clearStoragePrivateFilesBeforeDate(getDateBefore(), file)
    }

    private fun getDateBefore(): Date {
        val nowDate = Date()
        val calendar = Calendar.getInstance()
        calendar.time = nowDate
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - CLEAR_LOG_FILE_BEFORE)
        return calendar.time
    }
}

