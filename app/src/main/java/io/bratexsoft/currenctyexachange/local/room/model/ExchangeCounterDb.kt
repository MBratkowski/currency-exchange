package io.bratexsoft.currenctyexachange.local.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.bratexsoft.currenctyexachange.domain.model.ExchangeCounter
import java.util.*

@Entity(tableName = "exchangeCounter")
data class ExchangeCounterDb(
    @PrimaryKey val date: Long,
    @ColumnInfo(name = "exchangeCount") val exchangeCount: Int
) {
    companion object {
        fun toDb(exchangeCounter: ExchangeCounter): ExchangeCounterDb {
            return ExchangeCounterDb(
                exchangeCounter.date.time,
                exchangeCounter.counter
            )
        }
    }

    fun toDomain(): ExchangeCounter {
        return ExchangeCounter(
            date = Date(date),
            counter = exchangeCount
        )
    }
}