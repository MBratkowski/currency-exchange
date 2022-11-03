package io.bratexsoft.currenctyexachange.network.model


import com.google.gson.annotations.SerializedName
import io.bratexsoft.currenctyexachange.domain.model.Currency
import java.math.BigDecimal

data class CurrencyRatesResponseApi(
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("rates")
    val ratesMap: Map<Currency, BigDecimal>,
    @SerializedName("timestamp")
    val timestamp: Int
)