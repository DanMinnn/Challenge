package com.example.currencyconverter.network.repositories

import com.example.currencyconverter.models.CurrencyDTO
import com.example.currencyconverter.network.local.CurrencyRateLocal
import com.example.currencyconverter.network.local.HistoryLocal
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CurrencyRepository {

    suspend fun getExchangeRates(country: String): Response<CurrencyDTO>

    suspend fun getExchangeRatesFromLocal(country: String): Flow<CurrencyRateLocal>

    suspend fun insertCurrencyToLocal(currencyRateLocal: CurrencyRateLocal)

    suspend fun insertToHistory(historyLocal: List<HistoryLocal>)

    suspend fun getHistoryFromLocal() : Flow<List<HistoryLocal>>
}