package io.bratexsoft.currenctyexachange.local.room.client

import androidx.room.Database
import androidx.room.RoomDatabase
import io.bratexsoft.currenctyexachange.local.room.dao.CurrencyBalanceDao
import io.bratexsoft.currenctyexachange.local.room.dao.ExchangeCounterDao
import io.bratexsoft.currenctyexachange.local.room.dao.ExchangeEventDao
import io.bratexsoft.currenctyexachange.local.room.model.CurrencyBalanceDb
import io.bratexsoft.currenctyexachange.local.room.model.ExchangeCounterDb
import io.bratexsoft.currenctyexachange.local.room.model.ExchangeEventDb

@Database(
    entities = [
        CurrencyBalanceDb::class,
        ExchangeCounterDb::class,
        ExchangeEventDb::class],
    version = 1
)
abstract class CurrencyDatabaseClient : RoomDatabase() {
    abstract fun currencyBalanceDao(): CurrencyBalanceDao
    abstract fun exchangeCounterDao(): ExchangeCounterDao
    abstract fun exchangeEventDao(): ExchangeEventDao
}