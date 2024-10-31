package com.example.currencyconverter.network.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyconverter.network.local.CurrencyRateLocal
import com.example.currencyconverter.network.local.HistoryLocal

@Database(
    entities = [CurrencyRateLocal::class, HistoryLocal::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

    abstract fun historyDao(): HistoryDao
}