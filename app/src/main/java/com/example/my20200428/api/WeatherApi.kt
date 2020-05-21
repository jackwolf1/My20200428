package com.example.my20200428.api

import com.example.my20200428.retrofit.annotation.Field
import com.example.my20200428.retrofit.annotation.GET
import com.example.my20200428.retrofit.annotation.POST
import com.example.my20200428.retrofit.annotation.Query
import okhttp3.Call

interface WeatherApi {
    @POST("/v3/weather/weatherInfo")
    fun postWeather(@Field("city") city: String, @Field("key") key: String): Call


    @GET("/v3/weather/weatherInfo")
    fun getWeather(@Query("city") city: String, @Query("key") key: String): Call
}