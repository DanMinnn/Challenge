package com.example.currencyconverter.network.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyconverter.network.local.CurrencyRateLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExchangeRates(rates: CurrencyRateLocal)

    @Query("Select * from CurrencyRateLocal")
    fun getExchangeRateFromLocal(): Flow<CurrencyRateLocal>
}