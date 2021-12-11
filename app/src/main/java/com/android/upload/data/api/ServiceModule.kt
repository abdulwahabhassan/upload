package com.android.upload.data.api

import com.android.upload.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceModule {

    private val httpClient by lazy {
        val httpClientBuilder = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }

        httpClientBuilder.interceptors().add(httpLoggingInterceptor)
        httpClientBuilder.build()
    }


    private fun getRetrofit(baseUrl: String): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    fun createDarotApi(baseUrl: String): DarotApi {
        return getRetrofit(baseUrl).create(DarotApi::class.java)
    }


}