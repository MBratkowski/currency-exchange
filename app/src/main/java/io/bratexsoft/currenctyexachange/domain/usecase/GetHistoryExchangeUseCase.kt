package io.bratexsoft.currenctyexachange.domain.usecase

import io.bratexsoft.currenctyexachange.domain.model.ExchangeEvent
import io.bratexsoft.currenctyexachange.local.room.datasource.event.ExchangeEventLocalDataSource
import javax.inject.Inject

class GetHistoryExchangeUseCase @Inject constructor(
    private val eventLocalDataSource: ExchangeEventLocalDataSource
) {

    suspend operator fun invoke(): List<ExchangeEvent> {
        return eventLocalDataSource
            .getAllEvents()
            .sortedByDescending { it.date }
    }
}