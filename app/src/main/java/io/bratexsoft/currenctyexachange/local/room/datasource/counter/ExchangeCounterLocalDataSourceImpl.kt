package io.bratexsoft.currenctyexachange.local.room.datasource.counter

import io.bratexsoft.currenctyexachange.domain.model.ExchangeCounter
import io.bratexsoft.currenctyexachange.local.room.dao.ExchangeCounterDao
import io.bratexsoft.currenctyexachange.local.room.model.ExchangeCounterDb

class ExchangeCounterLocalDataSourceImpl(
    private val exchangeCounterDao: ExchangeCounterDao
) : ExchangeCounterLocalDataSource {

    override suspend fun saveCounterState(exchangeCounter: ExchangeCounter) {
        if (exchangeCounterDao.isRecordExist(exchangeCounter.date.time)) {
            exchangeCounterDao.updateExchangeCounter(ExchangeCounterDb.toDb(exchangeCounter))
        } else {
            exchangeCounterDao.insertExchangeCounter(ExchangeCounterDb.toDb(exchangeCounter))
        }
    }

    override suspend fun getCounterState(date: Long): Int {
        return exchangeCounterDao
            .getExchangeCounter(date)
            ?.toDomain()
            ?.counter ?: 0
    }
}