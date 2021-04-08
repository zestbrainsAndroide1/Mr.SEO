package com.zb.mrseo.restapi


import com.google.gson.GsonBuilder
import com.yalantis.ucrop.BuildConfig
import com.zb.moodlist.utility.ifNotNullOrElse

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level

import retrofit2.Retrofit
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiInitialize {

    private var retrofit: Retrofit? = null
    private var apiInIt: ApiInterface? = null


    fun initializes(): Retrofit {

        val gson = GsonBuilder()
                .setLenient()
                .create()

        retrofit = retrofit.ifNotNullOrElse({ it }, {
            Retrofit.Builder()
                    .baseUrl(WebConstant.BASEURL)
                    .client(requestHeader)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
        })
        return retrofit!!
    }


    fun initialize(): ApiInterface {

        val gson = GsonBuilder()
                .setLenient()
                .create()

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(WebConstant.BASEURL)
                    .client(requestHeader)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            apiInIt = retrofit!!.create(ApiInterface::class.java)
        }
        return apiInIt!!
    }


    private val requestHeader: OkHttpClient
        get() {
            return OkHttpClient.Builder()
                    .readTimeout(100, TimeUnit.SECONDS)
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor())
                    .build()
        }


    private fun loggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
    }
}
