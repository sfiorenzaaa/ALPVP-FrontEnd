package com.shannon.shannonweek14.data.service

import com.shannon.shannonweek14.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    fun getClient(token: String? = null): Retrofit {

        val clientBuilder = OkHttpClient.Builder()

        // Tambah interceptor kalau ada token
        if (!token.isNullOrEmpty()) {
            clientBuilder.addInterceptor(AuthInterceptor(token))
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
