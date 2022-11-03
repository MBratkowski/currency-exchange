package io.bratexsoft.currenctyexachange.network.api

import io.bratexsoft.currenctyexachange.network.model.CurrencyRatesResponseApi
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyServiceApi {

    @GET(value = "latest")
    suspend fun getLatestCurrencyRates(): CurrencyRatesResponseApi

    @GET(value = "latest")
    suspend fun getLatestCurrencyRates(@Query("base") baseCurrency: String): CurrencyRatesResponseApi
}