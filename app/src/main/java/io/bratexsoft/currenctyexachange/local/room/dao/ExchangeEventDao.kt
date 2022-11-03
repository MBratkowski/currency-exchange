package io.bratexsoft.currenctyexachange.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.bratexsoft.currenctyexachange.local.room.model.ExchangeEventDb

@Dao
interface ExchangeEventDao {

    @Insert
    suspend fun insertExchangeEvent(exchangeEventDb: ExchangeEventDb)

    @Query("SELECT * FROM exchangeHistory")
    suspend fun getAllExchangeEvents(): List<ExchangeEventDb>
}