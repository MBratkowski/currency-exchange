package io.bratexsoft.currenctyexachange.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.bratexsoft.currenctyexachange.local.prefs.CurrencyPrefs
import io.bratexsoft.currenctyexachange.local.prefs.CurrencyPrefsImpl
import io.bratexsoft.currenctyexachange.local.room.client.CurrencyDatabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideCurrencyPrefs(@ApplicationContext context: Context): CurrencyPrefs {
        return CurrencyPrefsImpl(
            sharedPreferences = context.getSharedPreferences(
                "currency_prefs",
                Context.MODE_PRIVATE
            )
        )
    }

    @Provides
    @Singleton
    fun provideCurrencyDatabase(
        @ApplicationContext context: Context
    ): CurrencyDatabaseClient {
        return Room.databaseBuilder(
            context,
            CurrencyDatabaseClient::class.java,
            "currency-database"
        ).build()
    }

}
