package io.bratexsoft.currenctyexachange.network.client

import android.util.Log
import io.bratexsoft.currenctyexachange.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        chain.request()
            .newBuilder()
            .apply {
                header("apikey", BuildConfig.apiKey)
            }.also {
                Log.d("Making request", "Making request")
                return chain.proceed(it.build())
            }
    }
}