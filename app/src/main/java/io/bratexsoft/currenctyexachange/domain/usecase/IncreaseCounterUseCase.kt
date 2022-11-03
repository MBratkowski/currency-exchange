package io.bratexsoft.currenctyexachange.domain.usecase

import io.bratexsoft.currenctyexachange.DateProvider
import io.bratexsoft.currenctyexachange.domain.model.ExchangeCounter
import io.bratexsoft.currenctyexachange.local.room.datasource.counter.ExchangeCounterLocalDataSource
import javax.inject.Inject

class IncreaseCounterUseCase @Inject constructor(
    private val exchangeCounterLocalDataSource: ExchangeCounterLocalDataSource,
    private val dateProvider: DateProvider,
) {

    suspend operator fun invoke() {
        updateCounter()
    }

    private suspend fun getExchangeCount(): Int {
        return exchangeCounterLocalDataSource.getCounterState(
            dateProvider.provideCurrentDateInNormalDate().time
        )
    }

    private suspend fun updateCounter() {
        exchangeCounterLocalDataSource.saveCounterState(
            ExchangeCounter(
                date = dateProvider.provideCurrentDateInNormalDate(),
                counter = getExchangeCount().inc()
            )
        )
    }
}