package io.bratexsoft.currenctyexachange.local.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.bratexsoft.currenctyexachange.domain.model.Currency
import io.bratexsoft.currenctyexachange.domain.model.ExchangeEvent
import java.math.BigDecimal
import java.util.*

@Entity(tableName = "exchangeHistory")
data class ExchangeEventDb(
    @PrimaryKey val date: Long,
    @ColumnInfo(name = "sellingCurrency") val sellingCurrency: String,
    @ColumnInfo(name = "sellingAmount") val sellingAmount: Double,
    @ColumnInfo(name = "receivingCurrency") val receivingCurrency: String,
    @ColumnInfo(name = "receivingAmount") val receivingAmount: Double,
    @ColumnInfo(name = "commissionFee") val commissionFee: Double,
) {
    companion object {
        fun toDb(exchangeEvent: ExchangeEvent): ExchangeEventDb {
            with(exchangeEvent) {
                return ExchangeEventDb(
                    date = date.time,
                    sellingCurrency = sellingCurrency.name,
                    sellingAmount = sellingAmount.toDouble(),
                    receivingCurrency = receivingCurrency.name,
                    receivingAmount = receivingAmount.toDouble(),
                    commissionFee = commissionFee.toDouble()
                )
            }
        }
    }

    fun toDomain(): ExchangeEvent {
        return ExchangeEvent(
            date = Date(date),
            sellingCurrency = Currency.valueOf(sellingCurrency),
            sellingAmount = BigDecimal(sellingAmount),
            receivingCurrency = Currency.valueOf(receivingCurrency),
            receivingAmount = BigDecimal(receivingAmount),
            commissionFee = BigDecimal(commissionFee)
        )
    }
}