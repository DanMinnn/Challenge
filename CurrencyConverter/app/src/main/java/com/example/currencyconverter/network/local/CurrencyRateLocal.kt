package com.example.currencyconverter.network.local

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.currencyconverter.models.ConversionRates
import com.example.currencyconverter.network.local.converters.TypeConverter

@Keep
@Entity
@TypeConverters(TypeConverter::class)
data class CurrencyRateLocal(
    @PrimaryKey @ColumnInfo(name = "base_code") val baseCode: String,
    @ColumnInfo(name = "conversionRates") val conversionRates: ConversionRates?,
    @ColumnInfo(name = "documentation") val documentation: String?,
    @ColumnInfo(name = "result") val result: String?,
    @ColumnInfo(name = "terms_of_use") val termsOfUse: String?,
    @ColumnInfo(name = "time_last_update_unix") val timeLastUpdateUnix: Int?,
    @ColumnInfo(name = "time_last_update_utc") val timeLastUpdateUtc: String?,
    @ColumnInfo(name = "time_next_update_unix") val timeNextUpdateUnix: Int?,
    @ColumnInfo(name = "time_next_update_utc") val timeNextUpdateUtc: String?,
)