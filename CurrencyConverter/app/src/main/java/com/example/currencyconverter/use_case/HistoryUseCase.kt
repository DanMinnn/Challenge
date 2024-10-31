package com.example.currencyconverter.use_case

import com.example.currencyconverter.network.local.HistoryLocal
import com.example.currencyconverter.network.repositories.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HistoryUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository,

    )  {
    operator fun invoke(historyLocal: List<HistoryLocal>): Flow<Boolean> = flow{
        currencyRepository.insertToHistory(historyLocal)

        emit(true)
    }

    fun getHistoryFromLocal(): Flow<List<HistoryLocal>> = flow{
        emit(currencyRepository.getHistoryFromLocal().first())
    }
}