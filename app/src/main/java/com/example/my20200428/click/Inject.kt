package com.example.my20200428.click

import android.app.Activity
import android.view.View

class Inject {
    fun inject(activity: Activity) {
        val cls = activity::class.java
        for (method in cls.methods) {
            if (method.isAnnotationPresent(Click::class.java)) {
                val inject = method.getAnnotation(Click::class.java)
                val id = inject.value
                val view = activity.findViewById<View>(id)
                view.setOnClickListener {
                    method.invoke(activity)
                }
            }
        }
    }
}