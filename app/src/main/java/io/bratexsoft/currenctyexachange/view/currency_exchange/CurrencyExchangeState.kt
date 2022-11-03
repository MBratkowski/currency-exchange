package io.bratexsoft.currenctyexachange.view.currency_exchange

import io.bratexsoft.currenctyexachange.domain.model.BalanceInformation
import io.bratexsoft.currenctyexachange.domain.model.Currency
import io.bratexsoft.currenctyexachange.domain.model.ExchangeEvent

data class CurrencyExchangeState(
    val sellingBalanceInformation: BalanceInformation = BalanceInformation(
        currency = Currency.EUR,
        amount = 0.toDouble()
    ),
    val receivingBalanceInformation: BalanceInformation = BalanceInformation(
        currency = Currency.USD,
        amount = 0.toDouble()
    ),
    val accountBalance: MutableMap<Currency, Double> = mutableMapOf(),
    val exchangeHistoryList: List<ExchangeEvent> = mutableListOf(),
    val exchangeButtonIsEnabled: Boolean = false,
) {
    fun getBalanceOfSellingCurrency(): Double {
        return accountBalance[sellingBalanceInformation.currency] ?: 0.toDouble()
    }
}
