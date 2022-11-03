package io.bratexsoft.currenctyexachange

import android.content.Context
import androidx.annotation.RawRes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import io.bratexsoft.currenctyexachange.domain.model.Currency
import java.math.BigDecimal
import javax.inject.Inject

class FakeJsonProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {

    fun loadCurrencies(): Map<Currency, BigDecimal> = readRawJson(R.raw.currency_response)

    private inline fun <reified T> readRawJson(@RawRes rawResId: Int): T {
        context.resources.openRawResource(rawResId).bufferedReader().use {
            return gson.fromJson(it, object : TypeToken<T>() {}.type)
        }
    }
}