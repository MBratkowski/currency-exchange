package io.bratexsoft.currenctyexachange

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.bratexsoft.currenctyexachange.domain.model.Currency
import io.bratexsoft.currenctyexachange.network.client.CurrencyDeserializer
import io.bratexsoft.currenctyexachange.network.model.CurrencyRatesResponseApi
import junit.framework.Assert.assertTrue
import org.junit.Test
import java.lang.reflect.Type
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Paths


class CurrencyDeserializerTest {

    @Test
    fun loadTestCase() {
        val json = readFileAsString("src/test/resources/currency_response.json")
        val type: Type = object : TypeToken<Map<Currency, BigDecimal>>() {}.type
        val gson = GsonBuilder()
            .registerTypeAdapter(type, CurrencyDeserializer())
            .create()

        val result = gson.fromJson(json, CurrencyRatesResponseApi::class.java)
        assertTrue(result.ratesMap.containsKey(Currency.USD))
    }


    private fun readFileAsString(file: String): String {
        return String(Files.readAllBytes(Paths.get(file)))
    }
}