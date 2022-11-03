package io.bratexsoft.currenctyexachange.local.room.datasource.balance

import io.bratexsoft.currenctyexachange.domain.model.BalanceInformation
import io.bratexsoft.currenctyexachange.local.room.dao.CurrencyBalanceDao
import io.bratexsoft.currenctyexachange.local.room.model.CurrencyBalanceDb

class CurrencyBalanceLocalDataSourceImpl(
    private val currencyBalanceDao: CurrencyBalanceDao
) : CurrencyBalanceLocalDataSource {

    override suspend fun getCurrencyBalanceList(): List<BalanceInformation> {
        return currencyBalanceDao.getCurrenciesBalance().map { it.toDomain() }
    }

    override suspend fun getCurrencyBalance(currency: String): BalanceInformation {
        return currencyBalanceDao.getCurrencyBalance(currency).toDomain()
    }

    override suspend fun saveCurrencyBalance(currencyBalance: BalanceInformation) {
        if (currencyBalanceDao.isRecordExist(currencyBalance.currency.name)) {
            currencyBalanceDao.updateCurrencyBalance(CurrencyBalanceDb.toDb(currencyBalance))
        } else {
            currencyBalanceDao.insertCurrencyBalance(CurrencyBalanceDb.toDb(currencyBalance))
        }
    }

    override suspend fun isCurrencyBalanceExist(currency: String): Boolean {
        return currencyBalanceDao.isRecordExist(currency)
    }

}