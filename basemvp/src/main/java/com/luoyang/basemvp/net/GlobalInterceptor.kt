package com.luoyang.basemvp.net

import com.luoyang.basemvp.utils.LogUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException
import java.nio.charset.Charset


/**
 * 通用网络拦截器
 *
 * @author luoyang
 * @date 2022/10/28
 */
class GlobalInterceptor : Interceptor {
    private var maxRetry: Int = 0
    private var retryNumber: Int = 0

    fun setMaxRetry(maxRetry: Int) {
        this.maxRetry = maxRetry
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url()
        LogUtils.d("GlobalInterceptor", "url: $url")
        var response = chain.proceed(request)
        LogUtils.d(
            "GlobalInterceptor",
            String.format(
                "...\n请求链接：%s\n请求参数：%s\n请求响应%s",
                request.url(),
                getRequestInfo(request),
                getResponseInfo(response)
            )

        )
        while (!response.isSuccessful && retryNumber < maxRetry) {
            retryNumber++
            response = chain.proceed(request)
        }
        return response
    }

    /**
     * 打印请求消息
     *
     * @param request 请求的对象
     */
    private fun getRequestInfo(request: Request?): String {
        var str = ""
        if (request == null) {
            return str
        }
        val requestBody: RequestBody = request.body() ?: return str
        try {
            val bufferedSink = okio.Buffer()
            requestBody.writeTo(bufferedSink)
            val charset: Charset = Charset.forName("utf-8")
            str = bufferedSink.readString(charset)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return str
    }

    /**
     * 响应消息
     *
     * @param response 返回的对象
     */
    private fun getResponseInfo(response: Response?): String {
        var str = ""
        if (response == null) {
            return str
        }
        val responseBody = response.body()
        if (!response.isSuccessful || (200 != response.code())||responseBody ==null) {
            return response.toString()
        }
        val contentLength = responseBody.contentLength()
        val source = responseBody.source()
        try {
            source.request(Long.MAX_VALUE) // Buffer the entire body.
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val buffer: okio.Buffer? = source.buffer()
        val charset: Charset = Charset.forName("utf-8")
        if (contentLength != 0L) {
            str = buffer?.clone()?.readString(charset) ?: ""
        }
        return str
    }

}