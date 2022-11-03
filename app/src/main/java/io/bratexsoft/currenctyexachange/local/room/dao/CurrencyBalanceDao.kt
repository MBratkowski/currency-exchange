package io.bratexsoft.currenctyexachange.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.bratexsoft.currenctyexachange.local.room.model.CurrencyBalanceDb

@Dao
interface CurrencyBalanceDao {
    @Query("SELECT * FROM currencyBalance")
    suspend fun getCurrenciesBalance(): List<CurrencyBalanceDb>

    @Query("SELECT * FROM currencyBalance WHERE currency LIKE :currency")
    suspend fun getCurrencyBalance(currency: String): CurrencyBalanceDb

    @Query("SELECT EXISTS(SELECT * FROM currencyBalance WHERE currency= :currency)")
    fun isRecordExist(currency: String): Boolean

    @Update
    suspend fun updateCurrencyBalance(currencyBalanceDb: CurrencyBalanceDb)

    @Insert
    suspend fun insertCurrencyBalance(currencyBalanceDb: CurrencyBalanceDb)

}