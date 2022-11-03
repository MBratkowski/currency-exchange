package io.bratexsoft.currenctyexachange.network.datasource

import io.bratexsoft.currenctyexachange.domain.model.Currency
import io.bratexsoft.currenctyexachange.network.api.CurrencyServiceApi
import java.math.BigDecimal
import javax.inject.Inject

class CurrencyNetworkDataSourceImpl @Inject constructor(
    private val currencyServiceApi: CurrencyServiceApi
) : CurrencyNetworkDataSource {
    override suspend fun getLatestCurrencyRates(): Map<Currency, BigDecimal> {
        return currencyServiceApi.getLatestCurrencyRates().ratesMap
    }

    override suspend fun getLatestCurrencyRates(baseCurrency: Currency): Map<Currency, BigDecimal> {
        return currencyServiceApi.getLatestCurrencyRates(baseCurrency.name).ratesMap
    }
}