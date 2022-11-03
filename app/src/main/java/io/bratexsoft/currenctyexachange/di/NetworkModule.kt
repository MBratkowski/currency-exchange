package io.bratexsoft.currenctyexachange.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.bratexsoft.currenctyexachange.domain.model.Currency
import io.bratexsoft.currenctyexachange.network.api.CurrencyServiceApi
import io.bratexsoft.currenctyexachange.network.client.CurrencyDeserializer
import io.bratexsoft.currenctyexachange.network.client.RetrofitClientProvider
import io.bratexsoft.currenctyexachange.network.datasource.CurrencyNetworkDataSource
import io.bratexsoft.currenctyexachange.network.datasource.CurrencyNetworkDataSourceImpl
import java.math.BigDecimal
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .registerTypeAdapter(
                object : TypeToken<Map<Currency, BigDecimal>>() {}.type,
                CurrencyDeserializer()
            ).create()
    }

    @Provides
    @Singleton
    fun provideRestClient(gson: Gson): CurrencyServiceApi {
        return RetrofitClientProvider.provideClient(
            "https://api.apilayer.com/exchangerates_data/",
            CurrencyServiceApi::class.java,
            gson = gson
        )
    }

    @Provides
    @Singleton
    fun provideNetworkDataSource(
        service: CurrencyServiceApi
    ): CurrencyNetworkDataSource {
        return CurrencyNetworkDataSourceImpl(service)
    }
}