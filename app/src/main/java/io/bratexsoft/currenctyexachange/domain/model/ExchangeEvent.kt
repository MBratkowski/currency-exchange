package io.bratexsoft.currenctyexachange.domain.model

import java.math.BigDecimal
import java.util.*

data class ExchangeEvent(
    val date: Date,
    val sellingCurrency: Currency,
    val sellingAmount: BigDecimal,
    val receivingCurrency: Currency,
    val receivingAmount: BigDecimal,
    val commissionFee: BigDecimal
)