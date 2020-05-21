package com.example.my20200428.retrofit

import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap
import okhttp3.HttpUrl.Companion.toHttpUrl

@Suppress("UNCHECKED_CAST")
class YingRetrofit constructor(val callFactory: Call.Factory, val baseUrl: HttpUrl) {

    private val serviceMethodCache = ConcurrentHashMap<Method, ServiceMethod>()

    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf(service),
            InvocationHandler { proxy: Any, method: Method, args: Array<Any> ->
                val serviceMethod = loadServiceMethod(method)
                return@InvocationHandler serviceMethod.invoke(args)
            }) as T
    }

    private fun loadServiceMethod(method: Method): ServiceMethod {

        return serviceMethodCache[method] ?: synchronized(serviceMethodCache) {
            var result = serviceMethodCache[method]
            if (result == null) {
                result = ServiceMethod.Builder(this, method).builder()
                serviceMethodCache[method] = result
            }
            return result
        }
    }

    class Builder constructor(
        val factory: Call.Factory? = null,
        val baseUrl: String
    ) {
        private var baseHttpUrl: HttpUrl = baseUrl.toHttpUrl()

        fun build(): YingRetrofit {
            val callFactory = factory ?: OkHttpClient()
            return YingRetrofit(callFactory, baseHttpUrl)
        }
    }
}
