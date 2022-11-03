package io.bratexsoft.currenctyexachange.view.currency_exchange

import io.bratexsoft.currenctyexachange.domain.model.Currency


sealed interface CurrencyExchangeViewEffect {
    object NoFundsToPayCommission:CurrencyExchangeViewEffect
    data class OpenCurrencyPicker(val mode: CurrencyPickerMode) : CurrencyExchangeViewEffect
    data class ShowCommissionFeeDialog(
        val sellingCurrency: Currency,
        val sellingAmount: Double,
        val receivingCurrency: Currency,
        val receivingAmount: Double,
        val commissionFeeAmount: Double
    ) : CurrencyExchangeViewEffect
}

enum class CurrencyPickerMode {
    SELL,
    RECEIVE
}