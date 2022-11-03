package io.bratexsoft.currenctyexachange

import io.bratexsoft.currenctyexachange.domain.model.ExchangeCounter
import io.bratexsoft.currenctyexachange.domain.usecase.CalculateCommissionFeeUseCase
import io.bratexsoft.currenctyexachange.local.room.datasource.counter.ExchangeCounterLocalDataSource
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class CommissionFeeTestCalculator {

    companion object {
        fun provideCounter(counterValue: Int) = ExchangeCounterLocalDataSourceFake(counterValue)
    }


    private val dateProvider = DateProvider()
    lateinit var calculator: CalculateCommissionFeeUseCase

    @Test
    fun `if counter of exchanges is below than 5 commission is zero`() = runBlocking {
        calculator = CalculateCommissionFeeUseCase(provideCounter(0), dateProvider)

        val result = calculator(100.0)

        assertEquals(BigDecimal(0.0), result)
    }

    @Test
    fun `if counter of exchanges is more than 5 but below 14 commission has value`() = runBlocking {
        calculator = CalculateCommissionFeeUseCase(provideCounter(7), dateProvider)

        val result = calculator(100.0)

        assertEquals(
            BigDecimal(0.7).setScale(2, RoundingMode.HALF_UP),
            result
        )
    }

    @Test
    fun `if counter of exchanges is more than 15 commission has value`() = runBlocking {
        calculator = CalculateCommissionFeeUseCase(provideCounter(15), dateProvider)

        val result = calculator(100.0)

        assertEquals(
            BigDecimal(1.5).setScale(2, RoundingMode.HALF_UP),
            result
        )
    }

}


class ExchangeCounterLocalDataSourceFake(
    private val counterValue: Int
) : ExchangeCounterLocalDataSource {
    override suspend fun saveCounterState(exchangeCounter: ExchangeCounter) {
        //Do nothing
    }

    override suspend fun getCounterState(date: Long): Int {
        return counterValue
    }

}