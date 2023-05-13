package com.ted.lib_network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response


/**
 * FileName: HttpInterceptor
 * Profile: 拦截器 / 重定向 请求头 加密/解密
 */
class HttpInterceptor : Interceptor {

    private val tag = "HTTP"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        Log.i(tag, "============REQUEST============")
        if(request.method() == "GET"){
            Log.i(tag, request.url().toString())
        }
        Log.i(tag, "============RESPONSE============")
        //response.body()?.let {
        //Log.i(tag, it.string())
        //}
        return response
    }
}