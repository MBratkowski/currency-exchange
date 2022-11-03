package io.bratexsoft.currenctyexachange.local.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.bratexsoft.currenctyexachange.domain.model.BalanceInformation
import io.bratexsoft.currenctyexachange.domain.model.Currency

@Entity(tableName = "currencyBalance")
data class CurrencyBalanceDb(
    @PrimaryKey val currency: String,
    @ColumnInfo(name = "balance") val balance: Double
) {

    companion object {
        fun toDb(currency: BalanceInformation): CurrencyBalanceDb {
            return CurrencyBalanceDb(
                currency = currency.currency.name,
                balance = currency.amount
            )
        }
    }

    fun toDomain(): BalanceInformation {
        return BalanceInformation(
            currency = Currency.valueOf(currency),
            amount = balance
        )
    }
}