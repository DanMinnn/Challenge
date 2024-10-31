package com.example.currencyconverter.network.local.converters

import androidx.room.TypeConverter
import com.example.currencyconverter.models.ConversionRates
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverter {

    @TypeConverter
    fun fromStringToRates(string: String?): ConversionRates? {
        return if (string == null) {
            null
        } else {
            val gson = Gson()
            val type = object : TypeToken<ConversionRates>() {}.type
            gson.fromJson(string, type)
        }
    }

    @TypeConverter
    fun fromRatesToString(images: ConversionRates?): String? {
        return if (images == null) {
            null
        } else {
            val gson = Gson()
            val type = object : TypeToken<ConversionRates>() {}.type
            gson.toJson(images, type)
        }
    }
}