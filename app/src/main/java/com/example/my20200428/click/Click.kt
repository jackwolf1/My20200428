package com.example.my20200428.click

import androidx.annotation.IdRes

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Click(@IdRes val value: Int)
