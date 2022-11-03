package io.bratexsoft.currenctyexachange.domain.usecase

import io.bratexsoft.currenctyexachange.DateProvider
import io.bratexsoft.currenctyexachange.domain.model.Currency
import io.bratexsoft.currenctyexachange.domain.model.ExchangeEvent
import io.bratexsoft.currenctyexachange.local.room.datasource.event.ExchangeEventLocalDataSource
import java.math.BigDecimal
import javax.inject.Inject

class SaveExchangeEventUseCase @Inject constructor(
    private val exchangeEventLocalDataSource: ExchangeEventLocalDataSource,
    private val dateProvider: DateProvider
) {

    suspend operator fun invoke(
        sellingCurrency: Currency,
        receivingCurrency: Currency,
        sellingAmount: BigDecimal,
        receivingAmount: BigDecimal,
        commissionFee: BigDecimal
    ) {
        exchangeEventLocalDataSource.saveEvent(
            ExchangeEvent(
                date = dateProvider.provideCurrentDateAndTime(),
                sellingCurrency = sellingCurrency,
                receivingCurrency = receivingCurrency,
                receivingAmount = receivingAmount,
                sellingAmount = sellingAmount,
                commissionFee = commissionFee
            )
        )
    }
}