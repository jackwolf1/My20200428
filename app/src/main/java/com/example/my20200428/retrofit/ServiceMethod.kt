package com.example.my20200428.retrofit

import com.example.my20200428.retrofit.annotation.Field
import com.example.my20200428.retrofit.annotation.GET
import com.example.my20200428.retrofit.annotation.POST
import com.example.my20200428.retrofit.annotation.Query
import okhttp3.Call
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.Request
import java.lang.reflect.Method

class ServiceMethod(val builder: Builder) {

    private val callFactory: Call.Factory
    private val relativeUrl: String
    private val hasBody: Boolean
    private val parameterHandler: Array<ParameterHandler?>
    private val baseUrl: HttpUrl
    private val httpMethod: String
    private val urlBuilder: HttpUrl.Builder
    private var formBuild: FormBody.Builder? = null

    init {
        baseUrl = builder.retrofit.baseUrl
        callFactory = builder.retrofit.callFactory
        httpMethod = builder.httpMethod
        relativeUrl = builder.relativeUrl
        hasBody = builder.hasBody
        parameterHandler = builder.parameterHandler
        if (hasBody) {
            formBuild = FormBody.Builder()
        }
        urlBuilder = baseUrl.newBuilder(relativeUrl)!!
    }

    fun invoke(args: Array<Any>): Any {
        for (i in 0 until parameterHandler.size) {
            val handlers = parameterHandler[i]
            handlers?.apply(this, args[i].toString())
        }
        val request = Request.Builder()
            .url(urlBuilder.build())
            .method(httpMethod, formBuild?.build())
            .build()
        return callFactory.newCall(request)
    }

    fun addQueryParameter(key: String, value: String) {
        urlBuilder.addQueryParameter(key, value)
    }

    fun addFieldParameter(key: String, value: String) {
        formBuild?.add(key, value)
    }

    class Builder constructor(val retrofit: YingRetrofit, method: Method) {
        private val methodAnnotations = method.annotations
        private val parameterAnnotations = method.parameterAnnotations
        internal lateinit var parameterHandler: Array<ParameterHandler?>
        internal lateinit var httpMethod: String
        internal lateinit var relativeUrl: String
        internal var hasBody: Boolean = false

        fun builder(): ServiceMethod {
            for (methodAnnotation in methodAnnotations) {
                when (methodAnnotation) {
                    is POST -> {
                        httpMethod = "POST"
                        relativeUrl = methodAnnotation.value
                        hasBody = true
                    }
                    is GET -> {
                        httpMethod = "GET"
                        relativeUrl = methodAnnotation.value
                        hasBody = false
                    }
                }
            }
            val length = parameterAnnotations.size
            parameterHandler = arrayOfNulls(length)
            for (i in 0 until length) {
                val annotations = parameterAnnotations[i]
                for (annotation in annotations) {
                    when (annotation) {
                        is Field -> {
                            val value = annotation.value
                            parameterHandler[i] = ParameterHandler.FieldParameterHandler(value)
                        }
                        is Query -> {
                            val value = annotation.value
                            parameterHandler[i] = ParameterHandler.QueryParameterHandler(value)
                        }
                    }
                }
            }
            return ServiceMethod(this)
        }
    }
}