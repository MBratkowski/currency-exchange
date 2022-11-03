package io.bratexsoft.currenctyexachange.network.datasource

import io.bratexsoft.currenctyexachange.domain.model.Currency
import java.math.BigDecimal

interface CurrencyNetworkDataSource {
    suspend fun getLatestCurrencyRates(): Map<Currency, BigDecimal>

    suspend fun getLatestCurrencyRates(baseCurrency: Currency): Map<Currency, BigDecimal>
}