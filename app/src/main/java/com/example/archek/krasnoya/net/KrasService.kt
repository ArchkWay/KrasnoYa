package com.example.archek.krasnoya.net


import retrofit2.Call
import retrofit2.http.GET


interface KrasService {//интерфейс с запросами из интернета

    @get:GET("bins/r2pdk")
    val data: Call<ObjectResponse>

}
