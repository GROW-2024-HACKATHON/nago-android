package com.grow.nago.remote

import com.grow.nago.BuildConfig
import com.grow.nago.remote.service.ReportService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.X509TrustManager

object RetrofitBuilder {
    private val retrofit: Retrofit by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okhttpBuilder = OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
        okhttpBuilder.hostnameVerifier { hostname, session -> true }
        okhttpBuilder.connectTimeout(30, TimeUnit.SECONDS)
        okhttpBuilder.readTimeout(30, TimeUnit.SECONDS)
        okhttpBuilder.writeTimeout(30, TimeUnit.SECONDS)
        val okHttpClient =  okhttpBuilder.build()
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val reportService: ReportService by lazy {
        retrofit.create(ReportService::class.java)
    }
}