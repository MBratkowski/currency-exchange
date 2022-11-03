package io.bratexsoft.currenctyexachange.network.client

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientProvider {
    fun <T> provideClient(host: String, api: Class<T>, gson: Gson): T {
        return Retrofit.Builder().client(
            OkHttpClient.Builder()
                .addInterceptor(HeaderInterceptor())
                .build()
        ).baseUrl(host).addConverterFactory(
            GsonConverterFactory.create(gson)
        ).build().create(api)
    }
}