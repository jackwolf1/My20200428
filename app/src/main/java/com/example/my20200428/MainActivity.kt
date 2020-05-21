package com.example.my20200428

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.my20200428.api.WeatherApi
import com.example.my20200428.click.Click
import com.example.my20200428.click.Inject
import com.example.my20200428.retrofit.YingRetrofit
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var weatherApi: WeatherApi


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val retrofit = YingRetrofit.Builder(baseUrl = "https://restapi.amap.com").build()
        weatherApi = retrofit.create(WeatherApi::class.java)
        Inject().inject(this)
    }

    fun yingGet(view: View) {

    }

    @Click(R.id.btn)
    fun testClick() {
        val call: Call =
            weatherApi.getWeather("110101", "ae6c53e2186f33bbf240a12d80672d1b")
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                Log.i(
                    "MainActivity",
                    "onResponse enjoy get: " + response.body?.string()
                )
                response.close()
            }
        })
    }

    @Click(R.id.btn1)
    fun testClick1() {
        val call: Call =
            weatherApi.postWeather("110101", "ae6c53e2186f33bbf240a12d80672d1b")
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                Log.i(
                    "MainActivity",
                    "onResponse enjoy post: " + response.body!!.string()
                )
                response.close()
            }
        })
    }
}
