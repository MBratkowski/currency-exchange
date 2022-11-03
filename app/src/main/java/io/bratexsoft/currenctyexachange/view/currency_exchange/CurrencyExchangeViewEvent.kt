package io.bratexsoft.currenctyexachange.view.currency_exchange

import io.bratexsoft.currenctyexachange.domain.model.Currency

sealed interface CurrencyExchangeViewEvent {
    data class UpdateCurrencies(
        val sellingCurrency: Currency? = null,
        val receivingCurrency: Currency? = null
    ) : CurrencyExchangeViewEvent
    object PickSellingCurrency : CurrencyExchangeViewEvent
    object PickReceiveCurrency : CurrencyExchangeViewEvent
    data class SellingAmountUpdated(val amount: String) : CurrencyExchangeViewEvent
    data class ReceivingAmountUpdated(val amount: String) : CurrencyExchangeViewEvent
    object Exchange : CurrencyExchangeViewEvent
}
