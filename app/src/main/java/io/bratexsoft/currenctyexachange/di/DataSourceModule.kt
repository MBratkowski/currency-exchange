package io.bratexsoft.currenctyexachange.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.bratexsoft.currenctyexachange.local.room.client.CurrencyDatabaseClient
import io.bratexsoft.currenctyexachange.local.room.datasource.balance.CurrencyBalanceLocalDataSource
import io.bratexsoft.currenctyexachange.local.room.datasource.balance.CurrencyBalanceLocalDataSourceImpl
import io.bratexsoft.currenctyexachange.local.room.datasource.counter.ExchangeCounterLocalDataSource
import io.bratexsoft.currenctyexachange.local.room.datasource.counter.ExchangeCounterLocalDataSourceImpl
import io.bratexsoft.currenctyexachange.local.room.datasource.event.ExchangeEventLocalDataSource
import io.bratexsoft.currenctyexachange.local.room.datasource.event.ExchangeEventLocalDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideCurrencyBalanceLocalDataSource(
        currencyDatabaseClient: CurrencyDatabaseClient
    ): CurrencyBalanceLocalDataSource {
        return CurrencyBalanceLocalDataSourceImpl(currencyDatabaseClient.currencyBalanceDao())
    }

    @Provides
    @Singleton
    fun provideExchangeEventLocalDataSource(
        currencyDatabaseClient: CurrencyDatabaseClient
    ): ExchangeEventLocalDataSource {
        return ExchangeEventLocalDataSourceImpl(currencyDatabaseClient.exchangeEventDao())
    }

    @Provides
    @Singleton
    fun provideExchangeCounterLocalDataSource(
        currencyDatabaseClient: CurrencyDatabaseClient
    ): ExchangeCounterLocalDataSource {
        return ExchangeCounterLocalDataSourceImpl(currencyDatabaseClient.exchangeCounterDao())
    }
}