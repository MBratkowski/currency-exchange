package io.bratexsoft.currenctyexachange.network.client

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import io.bratexsoft.currenctyexachange.domain.model.Currency
import java.lang.reflect.Type
import java.math.BigDecimal

class CurrencyDeserializer : JsonDeserializer<Map<Currency, BigDecimal>> {
    companion object {
        private const val RATES_JSON_OBJECT = "rates"
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Map<Currency, BigDecimal> {
        val currencyMap: MutableMap<Currency, BigDecimal> = mutableMapOf()
        json?.asJsonObject
            ?.get(RATES_JSON_OBJECT)
            ?.asJsonObject
            ?.entrySet()
            ?.forEach {
                val currency = Currency.valueOf(it.key)
                currencyMap[currency] = BigDecimal(it.value.asDouble)
            }
        return currencyMap

    }

}