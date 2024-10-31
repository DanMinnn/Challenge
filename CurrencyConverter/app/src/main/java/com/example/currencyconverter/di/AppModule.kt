package com.example.currencyconverter.di

import android.content.Context
import androidx.room.Room
import com.example.currencyconverter.Constants
import com.example.currencyconverter.network.CurrencyAPI
import com.example.currencyconverter.network.local.database.AppDatabase
import com.example.currencyconverter.network.local.database.CurrencyDao
import com.example.currencyconverter.network.local.database.HistoryDao
import com.example.currencyconverter.network.repositories.CurrencyRepository
import com.example.currencyconverter.network.repositories.CurrencyRepositoryImpl
import com.example.currencyconverter.utils.ApiLogger
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {

        val httpLoggingInterceptor = HttpLoggingInterceptor(ApiLogger())
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
            .readTimeout(15, TimeUnit.SECONDS).connectTimeout(15, TimeUnit.SECONDS).build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(gsonConverterFactory).client(okHttpClient).build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    @Provides
    @Singleton
    fun providesCurrencyAPI(
        gson: Gson, client: OkHttpClient
    ): CurrencyAPI {
        return Retrofit.Builder().client(client)
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
            .create(CurrencyAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesCurrency(
        api: CurrencyAPI, currencyDao: CurrencyDao, historyDao: HistoryDao
    ): CurrencyRepository {
        return CurrencyRepositoryImpl(api, currencyDao, historyDao)
    }

    @Provides
    @Singleton
    fun provideCurrencyDao(appDatabase: AppDatabase): CurrencyDao {
        return appDatabase.currencyDao()
    }

    @Provides
    @Singleton
    fun provideHistoryDao(appDatabase: AppDatabase): HistoryDao {
        return appDatabase.historyDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java, "currency-db"
        ).fallbackToDestructiveMigration().build()
    }
}