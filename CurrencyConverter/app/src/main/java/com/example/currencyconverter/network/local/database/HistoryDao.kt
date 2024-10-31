package com.example.currencyconverter.network.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyconverter.network.local.HistoryLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun pushToHistory(historyLocal: List<HistoryLocal>)

    @Query("Select * from HistoryLocal")
    fun getHistoryList(): Flow<List<HistoryLocal>>
}