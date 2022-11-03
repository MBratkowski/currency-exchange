package io.bratexsoft.currenctyexachange.local.room.datasource.balance

import io.bratexsoft.currenctyexachange.domain.model.BalanceInformation

interface CurrencyBalanceLocalDataSource {

    suspend fun getCurrencyBalanceList(): List<BalanceInformation>

    suspend fun getCurrencyBalance(currency: String): BalanceInformation

    suspend fun saveCurrencyBalance(currencyBalance: BalanceInformation)

    suspend fun isCurrencyBalanceExist(currency: String): Boolean

}