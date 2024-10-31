package com.example.currencyconverter.presentation

import com.example.currencyconverter.models.CurrencyConvert
import com.example.currencyconverter.network.local.HistoryLocal
import com.example.currencyconverter.utils.NetworkResult

data class CurrencyState(
    val currencyRate: CurrencyConvert = CurrencyConvert(),
    val baseCurrency: String = "",
    val defaultCurrency: String = "",
    val toCurrency: String = "",
    var amount: String = "",
    val convertedValue: String = "",
    val singleConvertedValue: String = "",
    val historyList: MutableList<HistoryLocal> = mutableListOf(),
    val isLoading: Boolean = false,
    val networkResult: NetworkResult<Any>? = null
)