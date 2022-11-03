package io.bratexsoft.currenctyexachange.domain.usecase

import io.bratexsoft.currenctyexachange.domain.model.Currency
import io.bratexsoft.currenctyexachange.network.datasource.CurrencyNetworkDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class GetCurrencyRatesUseCase @Inject constructor(
    private val dataSource: CurrencyNetworkDataSource
) {
    companion object {
        private const val DELAY = 60
    }

    suspend operator fun invoke(currency: Currency): Flow<Map<Currency, BigDecimal>> {
        return flow {
            while (true) {
                emit(dataSource.getLatestCurrencyRates(currency))
                delay(DELAY.seconds.inWholeMilliseconds)
            }
        }
    }
}