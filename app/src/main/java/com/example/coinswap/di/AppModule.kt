package com.example.coinswap.di

import android.app.Application
import androidx.room.Room
import com.example.coinswap.data.local.remote.repository.CurrencyApi
import com.example.coinswap.data.local.remote.repository.CurrencyRateDatabase
import com.example.coinswap.data.local.remote.repository.Dto.CurrencyDto
import com.example.coinswap.data.local.remote.repository.RepositoryImpe
import com.example.coinswap.domain.model.repository.CurrencyRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent:: class)
object AppModule {
    @Provides
    @Singleton
    fun provideCurrencyApi(): CurrencyApi{
        val retrofit = Retrofit
            .Builder()
            .baseUrl(CurrencyApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return  retrofit.create(CurrencyApi::class.java)
    }

    @Provides
    @Singleton
    fun providesDatabase(application: Application): CurrencyRateDatabase{
        return Room.databaseBuilder(
            application,
            CurrencyRateDatabase::class.java,
            "currency_db"
        )
            .build()
    }
    @Provides
    @Singleton
    fun provideRepository(
        api: CurrencyApi,
        db: CurrencyRateDatabase
    ): CurrencyRepository{
        return RepositoryImpe(
            api = api,
            dao = db.currencyRateDao
        )
    }
}