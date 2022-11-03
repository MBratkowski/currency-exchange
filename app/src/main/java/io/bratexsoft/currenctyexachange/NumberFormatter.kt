package io.bratexsoft.currenctyexachange

import java.text.DecimalFormat

object NumberFormatter {
    private val numberFormatter = DecimalFormat("#.##")

    fun provideNumberFormatter() = numberFormatter
}