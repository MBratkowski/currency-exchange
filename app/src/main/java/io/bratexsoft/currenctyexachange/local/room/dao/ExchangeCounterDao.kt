package io.bratexsoft.currenctyexachange.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.bratexsoft.currenctyexachange.local.room.model.ExchangeCounterDb

@Dao
interface ExchangeCounterDao {
    @Insert
    suspend fun insertExchangeCounter(exchangeCounterDb: ExchangeCounterDb)

    @Update
    suspend fun updateExchangeCounter(exchangeCounterDb: ExchangeCounterDb)

    @Query("SELECT * FROM exchangeCounter WHERE date LIKE :date LIMIT 1")
    suspend fun getExchangeCounter(date: Long): ExchangeCounterDb?

    @Query("SELECT EXISTS(SELECT * FROM exchangeCounter WHERE date= :date)")
    fun isRecordExist(date: Long): Boolean
}