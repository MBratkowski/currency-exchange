package io.bratexsoft.currenctyexachange.domain.usecase

import io.bratexsoft.currenctyexachange.domain.error.BalanceIsNegativeException
import io.bratexsoft.currenctyexachange.domain.error.CurrencyBalanceNotFoundException
import io.bratexsoft.currenctyexachange.domain.model.BalanceInformation
import io.bratexsoft.currenctyexachange.domain.model.Currency
import io.bratexsoft.currenctyexachange.local.room.datasource.balance.CurrencyBalanceLocalDataSource
import java.math.BigDecimal
import javax.inject.Inject

class ExchangeCurrencyUseCase @Inject constructor(
    private val currencyBalanceLocalDataSource: CurrencyBalanceLocalDataSource,
    private val calculateCommissionFeeUseCase: CalculateCommissionFeeUseCase,
    private val increaseCounterUseCase: IncreaseCounterUseCase,
    private val saveExchangeEventUseCase: SaveExchangeEventUseCase
) {

    suspend operator fun invoke(
        sellingCurrency: Currency,
        receivingCurrency: Currency,
        amount: Double,
        rate: Double
    ): BigDecimal {
        checkIfCurrencyExist(sellingCurrency)
        val commissionFee = calculateCommissionFeeUseCase(amount)
        val receivingBalance = BigDecimal(amount).multiply(BigDecimal(rate))
        saveExchangeEventUseCase(
            sellingAmount = BigDecimal(amount),
            sellingCurrency = sellingCurrency,
            receivingAmount = receivingBalance,
            receivingCurrency = receivingCurrency,
            commissionFee = commissionFee
        )
        updateSellingCurrencyBalance(amount, commissionFee.toDouble(), sellingCurrency)
        updateReceivingCurrencyBalance(receivingBalance, receivingCurrency)

        increaseCounterUseCase()

        return commissionFee
    }

    private suspend fun checkIfCurrencyExist(currentCurrency: Currency) {
        if (!currencyBalanceLocalDataSource.isCurrencyBalanceExist(currentCurrency.name)) {
            throw CurrencyBalanceNotFoundException()
        }
    }

    private suspend fun updateSellingCurrencyBalance(
        amount: Double,
        commissionFee: Double,
        sellingCurrency: Currency
    ) {
        val currentBalance = currencyBalanceLocalDataSource.getCurrencyBalance(sellingCurrency.name)
        val newBalance = BigDecimal(currentBalance.amount).minus(
            BigDecimal(amount).plus(BigDecimal(commissionFee))
        )
        if (newBalance.toDouble() < 0) {
            throw BalanceIsNegativeException()
        }
        currencyBalanceLocalDataSource.saveCurrencyBalance(
            BalanceInformation(sellingCurrency, newBalance.toDouble())
        )
    }

    private suspend fun updateReceivingCurrencyBalance(
        receivingBalance: BigDecimal,
        receivingCurrency: Currency
    ): BigDecimal {
        var currentBalance = BigDecimal(0)
        if (currencyBalanceLocalDataSource.isCurrencyBalanceExist(receivingCurrency.name)) {
            currentBalance =
                BigDecimal(currencyBalanceLocalDataSource.getCurrencyBalance(receivingCurrency.name).amount)
        }

        val newBalance = receivingBalance.plus(currentBalance)
        currencyBalanceLocalDataSource.saveCurrencyBalance(
            BalanceInformation(receivingCurrency, newBalance.toDouble())
        )
        return newBalance
    }

}