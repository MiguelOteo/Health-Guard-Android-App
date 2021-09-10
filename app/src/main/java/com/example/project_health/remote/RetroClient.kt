package com.example.project_health.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetroClient {
    private var retrofit: Retrofit? = null
    fun getClient(baseUrl: String): Retrofit {

        val gson = GsonBuilder().setLenient().create()

        if(retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit!!
    }
}