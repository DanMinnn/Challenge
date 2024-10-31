package com.example.currencyconverter.network.local

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.currencyconverter.network.local.converters.TypeConverter

@Keep
@Entity
@TypeConverters(TypeConverter::class)
data class HistoryLocal(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "from_currency") val fromCurrency: String?,
    @ColumnInfo(name = "from") val from: String?,
    @ColumnInfo(name = "to_currency") val toCurrency: String?,
    @ColumnInfo(name = "to") val to: String?,
)