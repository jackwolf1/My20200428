package com.example.my20200428.retrofit

abstract class ParameterHandler {
    abstract fun apply(serviceMethod: ServiceMethod, value: String)
    class QueryParameterHandler constructor(val key: String) : ParameterHandler() {
        override fun apply(serviceMethod: ServiceMethod, value: String) {
            serviceMethod.addQueryParameter(key, value)
        }
    }

    class FieldParameterHandler(val key: String) : ParameterHandler() {
        override fun apply(serviceMethod: ServiceMethod, value: String) {
            serviceMethod.addFieldParameter(key, value)
        }

    }
}