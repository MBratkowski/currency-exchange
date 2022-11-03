package io.bratexsoft.currenctyexachange.view.currency_search

sealed interface CurrencyPickerEvent {
    object LoadCurrencies: CurrencyPickerEvent
}