package io.bratexsoft.currenctyexachange.local.room.datasource.event

import io.bratexsoft.currenctyexachange.domain.model.ExchangeEvent
import io.bratexsoft.currenctyexachange.local.room.dao.ExchangeEventDao
import io.bratexsoft.currenctyexachange.local.room.model.ExchangeEventDb

class ExchangeEventLocalDataSourceImpl(
    private val exchangeEventDao: ExchangeEventDao
) : ExchangeEventLocalDataSource {

    override suspend fun saveEvent(exchangeEvent: ExchangeEvent) {
        exchangeEventDao.insertExchangeEvent(
            ExchangeEventDb.toDb(exchangeEvent)
        )
    }

    override suspend fun getAllEvents(): List<ExchangeEvent> {
        return exchangeEventDao
            .getAllExchangeEvents()
            .map { it.toDomain() }
    }
}