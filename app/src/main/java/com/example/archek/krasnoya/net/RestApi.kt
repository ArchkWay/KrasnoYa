package com.example.archek.krasnoya.net

import com.example.archek.krasnoya.BuildConfig
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestApi {
    private val BASE_URL = "https://api.myjson.com/"//  ссылка на апи
    private val CONNECT_TIMEOUT: Long = 60// задержка для загрузки
    private val READ_TIMEOUT: Long = 60

    private val retrofit = Retrofit.Builder() //выставляем билдер для ретрофита
            .baseUrl(BASE_URL)
            .validateEagerly(true)
            .client(buildOkHttpClient())
            .addConverterFactory(createConverterFactory())
            .build()

    fun <S> createService(klass: Class<S>): S {//метод для загрузки данных
        return retrofit.create(klass)
    }

    private fun createConverterFactory(): Converter.Factory {//gson конвертер
        return GsonConverterFactory.create(
                GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()
        )
    }

    private fun buildOkHttpClient(): OkHttpClient { //устанавливаем http клиент
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }
        return builder.build()
    }
}
