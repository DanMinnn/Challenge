package com.example.currencyconverter.network.repositories

import com.example.currencyconverter.models.CurrencyDTO
import com.example.currencyconverter.network.CurrencyAPI
import com.example.currencyconverter.network.local.CurrencyRateLocal
import com.example.currencyconverter.network.local.HistoryLocal
import com.example.currencyconverter.network.local.database.CurrencyDao
import com.example.currencyconverter.network.local.database.HistoryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val api: CurrencyAPI,
    private val currencyDao: CurrencyDao,
    private val historyDao: HistoryDao
): CurrencyRepository {

    override suspend fun getExchangeRates(country: String): Response<CurrencyDTO> {
        return api.getExchangeRates(country)
    }

    override suspend fun getExchangeRatesFromLocal(country: String): Flow<CurrencyRateLocal> {
        return currencyDao.getExchangeRateFromLocal()
    }

    override suspend fun insertCurrencyToLocal(currencyRateLocal: CurrencyRateLocal) {
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
            currencyDao.insertExchangeRates(currencyRateLocal)
        }
    }

    override suspend fun insertToHistory(historyLocal: List<HistoryLocal>) {
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
            historyDao.pushToHistory(historyLocal)
        }
    }

    override suspend fun getHistoryFromLocal(): Flow<List<HistoryLocal>> {
        return historyDao.getHistoryList()
    }

}