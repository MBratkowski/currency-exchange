package io.bratexsoft.currenctyexachange.local.prefs

interface CurrencyPrefs {
    fun isFirstLaunch(): Boolean
    fun updateFirstLaunch()
}