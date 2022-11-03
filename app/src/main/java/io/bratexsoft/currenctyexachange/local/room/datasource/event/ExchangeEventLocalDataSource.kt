package io.bratexsoft.currenctyexachange.local.room.datasource.event

import io.bratexsoft.currenctyexachange.domain.model.ExchangeEvent

interface ExchangeEventLocalDataSource {

    suspend fun saveEvent(exchangeEvent: ExchangeEvent)

    suspend fun getAllEvents(): List<ExchangeEvent>
}