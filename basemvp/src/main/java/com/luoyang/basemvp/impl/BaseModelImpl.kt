package com.luoyang.basemvp.impl

import com.google.gson.GsonBuilder
import com.luoyang.basemvp.net.GlobalInterceptor
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 联网请求 Retrofit基础，需实现
 *
 * @author luoyang
 * @date 2022/10/28
 */
object BaseModelImpl {

    private val okHttpBuilder: OkHttpClient.Builder =
        OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS)

    private val retrofitCache: Hashtable<String, Retrofit> = Hashtable()
    private val managedDisposables: MutableList<WeakReference<Disposable>> = mutableListOf()

    private fun getRetrofit(url: String): Retrofit {
        if (retrofitCache.containsKey(url)) {
            return retrofitCache.getValue(url)
        }
        val converter = GsonConverterFactory.create(GsonBuilder().setLenient().create())
        val adapter = RxJava2CallAdapterFactory.create()
        val builder = Retrofit.Builder()
        val interceptor = GlobalInterceptor()
        val client: OkHttpClient = okHttpBuilder.addInterceptor(interceptor).build()
        val retrofit =
            builder.baseUrl(url).addConverterFactory(converter).addCallAdapterFactory(adapter)
                .client(client).build()
        retrofitCache[url] = retrofit
        return retrofit
    }


    /**
     * 创建一个Retrofit Api接口实例
     *
     * @param url 请求地址
     * @param apiClass Http api 接口定义
     * @return Http Api接口实例，可直接调用方法
     */
    fun <T> createService(url: String, apiClass: Class<T>): T = getRetrofit(url).create(apiClass)

    /**
     * 托管所有已创建的http observable
     */
    fun manage(disposable: Disposable) {
        managedDisposables.add(WeakReference(disposable))
    }

    /**
     * 销毁请求服务
     */
    fun dispose() {
        retrofitCache.clear()
        managedDisposables.forEach {
            with(it.get()) {
                if (this != null && !isDisposed) {
                    dispose()
                }
            }
        }
        managedDisposables.clear()
    }
}