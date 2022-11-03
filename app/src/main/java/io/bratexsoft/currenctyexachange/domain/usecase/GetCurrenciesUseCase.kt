package io.bratexsoft.currenctyexachange.domain.usecase

import io.bratexsoft.currenctyexachange.domain.model.Currency
import io.bratexsoft.currenctyexachange.network.datasource.CurrencyNetworkDataSource
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val networkDataSource: CurrencyNetworkDataSource
) {

    suspend operator fun invoke(): Set<Currency> {
        return networkDataSource.getLatestCurrencyRates().keys
    }
}