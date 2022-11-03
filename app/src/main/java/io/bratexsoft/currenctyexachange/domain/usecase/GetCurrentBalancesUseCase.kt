package io.bratexsoft.currenctyexachange.domain.usecase

import io.bratexsoft.currenctyexachange.domain.model.BalanceInformation
import io.bratexsoft.currenctyexachange.domain.model.Currency
import io.bratexsoft.currenctyexachange.local.prefs.CurrencyPrefs
import io.bratexsoft.currenctyexachange.local.room.datasource.balance.CurrencyBalanceLocalDataSource
import javax.inject.Inject

class GetCurrentBalancesUseCase @Inject constructor(
    private val currencyBalanceLocalDataSource: CurrencyBalanceLocalDataSource,
    private val currencyPrefs: CurrencyPrefs
) {
    suspend operator fun invoke(): Map<Currency, Double> {
        if (currencyPrefs.isFirstLaunch()) {
            currencyBalanceLocalDataSource.saveCurrencyBalance(
                BalanceInformation(
                    currency = Currency.EUR,
                    amount = 1000.0
                )
            )
            currencyPrefs.updateFirstLaunch()
        }
        return currencyBalanceLocalDataSource
            .getCurrencyBalanceList()
            .associate { it.currency to it.amount }
    }
}