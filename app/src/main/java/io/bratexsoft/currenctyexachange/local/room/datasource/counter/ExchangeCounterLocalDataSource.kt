package io.bratexsoft.currenctyexachange.local.room.datasource.counter

import io.bratexsoft.currenctyexachange.domain.model.ExchangeCounter

interface ExchangeCounterLocalDataSource {

    suspend fun saveCounterState(exchangeCounter: ExchangeCounter)

    suspend fun getCounterState(date : Long): Int
}