package io.bratexsoft.currenctyexachange.view.currency_search

import io.bratexsoft.currenctyexachange.domain.model.Currency

data class CurrencyPickerState(
    val currenciesSet: Set<Currency> = emptySet()
)