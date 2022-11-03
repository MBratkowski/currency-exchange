package io.bratexsoft.currenctyexachange

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateProvider @Inject constructor() {
    private val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    fun provideCurrentDateInNormalDate(): Date {
        return formatter.parse(formatter.format(Date()))!!
    }

    fun provideCurrentDateAndTime(): Date {
        return Date()
    }
}