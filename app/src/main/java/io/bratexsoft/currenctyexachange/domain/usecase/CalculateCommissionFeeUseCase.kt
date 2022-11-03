package io.bratexsoft.currenctyexachange.domain.usecase

import io.bratexsoft.currenctyexachange.DateProvider
import io.bratexsoft.currenctyexachange.local.room.datasource.counter.ExchangeCounterLocalDataSource
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class CalculateCommissionFeeUseCase @Inject constructor(
    private val exchangeCounterLocalDataSource: ExchangeCounterLocalDataSource,
    private val dateProvider: DateProvider
) {

    suspend operator fun invoke(
        value: Double,
    ): BigDecimal {
        val exchangeCount =
            exchangeCounterLocalDataSource.getCounterState(dateProvider.provideCurrentDateInNormalDate().time)
        return when {
            exchangeCount < 5 -> BigDecimal(0)
            exchangeCount in 5..14 -> BigDecimal(value).multiply(BigDecimal(0.007))
                .setScale(2, RoundingMode.HALF_UP)
            else -> BigDecimal(value).multiply(BigDecimal(0.012)).plus(BigDecimal(0.3))
                .setScale(2, RoundingMode.HALF_UP)
        }
    }
}