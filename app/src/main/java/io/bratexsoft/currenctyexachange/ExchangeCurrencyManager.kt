package io.bratexsoft.currenctyexachange

import io.bratexsoft.currenctyexachange.domain.model.Currency
import io.bratexsoft.currenctyexachange.domain.usecase.GetCurrencyRatesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class ExchangeCurrencyManager @Inject constructor(
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase
) {
    private val sellingCurrencyRates: MutableMap<Currency, BigDecimal> = mutableMapOf()
    private var sellingCurrencyRatesFlowJob: Job? = null

    fun subscribeRates(scope: CoroutineScope, currency: Currency) {
        sellingCurrencyRatesFlowJob = scope.launch {
            getCurrencyRatesUseCase(currency)
                .cancellable()
                .collectLatest {
                    sellingCurrencyRates.clear()
                    sellingCurrencyRates.putAll(it)
                }
        }
    }

    fun provideRatesForCurrency(currency: Currency): Double {
        return sellingCurrencyRates[currency]?.toDouble() ?: 0.0
    }

    fun resubscribeRates(scope: CoroutineScope, currency: Currency) {
        sellingCurrencyRatesFlowJob?.cancel()
        subscribeRates(scope, currency)
    }

    fun calculateReceivingAmount(
        sellAmount: Double,
        currency: Currency
    ): Double {
        return  if(sellingCurrencyRates.containsKey(currency)) {
             BigDecimal(sellAmount).multiply(
                sellingCurrencyRates[
                        currency
                ]
            ).toDouble()
        } else{
            0.0
        }
    }

    fun calculateSellingAmount(
        receivingAmount: Double,
        currency: Currency
    ): Double {
        return BigDecimal(receivingAmount).divide(
            sellingCurrencyRates[
                    currency
            ], RoundingMode.HALF_UP
        ).toDouble()
    }
}