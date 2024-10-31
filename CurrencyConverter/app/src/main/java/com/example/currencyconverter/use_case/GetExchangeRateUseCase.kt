package com.example.currencyconverter.use_case

import android.content.Context
import com.example.currencyconverter.models.CurrencyConvert
import com.example.currencyconverter.models.toConvert
import com.example.currencyconverter.models.toLocal
import com.example.currencyconverter.network.repositories.CurrencyRepository
import com.example.currencyconverter.utils.Constants.Companion.checkInternetConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetExchangeRateUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository,
) {

    operator fun invoke(country: String, context: Context): Flow<CurrencyConvert> = flow{
        if (checkInternetConnection(context)){
            val response = currencyRepository.getExchangeRates(country)

            if (response.isSuccessful){
                response.body()?.toLocal()?.let { currencyRepository.insertCurrencyToLocal(it) }
                emit(value = response.body()?.toConvert() ?: CurrencyConvert())
            }
        }  else {
            val localData = currencyRepository.getExchangeRatesFromLocal(country)
            val data = localData.first()

            emit(value = CurrencyConvert(
                baseCode = data.baseCode,
                conversionRates = data.conversionRates,
                documentation = data.documentation,
                result = data.result,
                termsOfUse = data.termsOfUse,
                timeLastUpdateUnix = data.timeLastUpdateUnix,
                timeLastUpdateUtc = data.timeLastUpdateUtc,
                timeNextUpdateUnix = data.timeNextUpdateUnix
            ))
        }
    }
}