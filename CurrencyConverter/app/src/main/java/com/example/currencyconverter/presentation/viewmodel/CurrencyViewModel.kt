package com.example.currencyconverter.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.models.ConversionRates
import com.example.currencyconverter.network.local.HistoryLocal
import com.example.currencyconverter.network.local.PreferenceDataUtils
import com.example.currencyconverter.presentation.CurrencyState
import com.example.currencyconverter.use_case.GetExchangeRateUseCase
import com.example.currencyconverter.use_case.HistoryUseCase
import com.example.currencyconverter.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getExchangeRateUseCase: GetExchangeRateUseCase,
    private val historyUseCase: HistoryUseCase,
    private val preferenceDataStoreUtils: PreferenceDataUtils,
    private val application: Application
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(CurrencyState())
    var state = _state.asStateFlow()

    init {
        fetchHistoryFromLocal()
        viewModelScope.launch {
            _state.update {
                it.copy(
                    defaultCurrency = preferenceDataStoreUtils.getDefault().first(),
                    baseCurrency = preferenceDataStoreUtils.getDefault().first()
                )
            }
            getExchangeRatesSafeCall(preferenceDataStoreUtils.getDefault().first())
        }
    }

    fun updateDropDownValues(countryCurrencyCode: String, isForBase: String) {
        if (isForBase == "FROM") {
            getExchangeRatesSafeCall(value = countryCurrencyCode)
        }
        _state.update {
            when (isForBase) {
                "FROM" -> {
                    it.copy(
                        baseCurrency = countryCurrencyCode
                    )
                }

                "DEFAULT" -> {
                    it.copy(
                        defaultCurrency = countryCurrencyCode,
                        baseCurrency = countryCurrencyCode
                    )
                }

                else -> {
                    it.copy(
                        toCurrency = countryCurrencyCode
                    )
                }
            }
        }

        if (isForBase == "DEFAULT") {
            getExchangeRatesSafeCall(countryCurrencyCode)
            viewModelScope.launch {
                preferenceDataStoreUtils.setDefault(countryCurrencyCode)
            }
        }
    }

    fun updateAmount(value: String) {

        _state.update {
            try {
                val amountValue = if (value.isNotEmpty()) value.toDouble() else 0.0
                it.copy(
                    amount = value,
                    convertedValue = if (value.isNotEmpty()) {
                        "${
                            getOutputString(
                                if (amountValue == 1.0) getToValue(
                                    it.toCurrency,
                                    it.currencyRate.conversionRates
                                )
                                else amountValue * getToValue(
                                    it.toCurrency,
                                    it.currencyRate.conversionRates
                                )
                            )
                        } ${it.toCurrency}"
                    } else "",
                    singleConvertedValue = "1 ${it.baseCurrency} = ${
                        getOutputString(getToValue(it.toCurrency, it.currencyRate.conversionRates))
                    } ${it.toCurrency}"
                )
            } catch (e: NumberFormatException) {
                it.copy(
                    amount = value,
                    convertedValue = "",
                    singleConvertedValue = it.singleConvertedValue
                )
            }
        }
    }

    fun swapTargets() {
        getExchangeRatesSafeCall(state.value.toCurrency) {
            val temp = state.value.baseCurrency
            _state.update {
                it.copy(
                    baseCurrency = it.toCurrency,
                    toCurrency = temp,
                    amount = it.amount,
                    convertedValue = "${
                        getOutputString(
                            if (it.amount == "1") getToValue(
                                temp,
                                it.currencyRate.conversionRates
                            ) else it.amount.toDouble() * getToValue(
                                temp,
                                it.currencyRate.conversionRates
                            )
                        )
                    } ${temp}",
                    singleConvertedValue = "1 ${it.toCurrency} = ${
                        getOutputString(
                            getToValue(
                                temp,
                                it.currencyRate.conversionRates
                            )
                        )
                    } ${temp}"
                )
            }
        }
    }

    fun doCalculation() {
        _state.update {
            it.copy(
                convertedValue = "${
                    getOutputString(
                        it.amount.toDouble() * getToValue(
                            it.toCurrency,
                            it.currencyRate.conversionRates
                        )
                    )
                } ${it.toCurrency}",
                singleConvertedValue = "1 ${it.baseCurrency} = ${
                    getOutputString(
                        getToValue(
                            it.toCurrency,
                            it.currencyRate.conversionRates
                        )
                    )
                } ${it.toCurrency}"
            )
        }

        pushDataToLocal()
    }

    private fun pushDataToLocal() {
        _state.update {
            it.copy(
                historyList = it.historyList.plus(
                    HistoryLocal(
                        state.value.historyList.size + 1,
                        fromCurrency = state.value.baseCurrency,
                        from = state.value.amount,
                        toCurrency = state.value.toCurrency,
                        to = state.value.convertedValue
                    )
                ).toMutableList()
            )
        }

        historyUseCase.invoke(
            historyLocal = state.value.historyList
        ).onEach {
            Log.e("RESULT IS:", it.toString())
            fetchHistoryFromLocal()
        }.launchIn(viewModelScope)
    }


    private fun fetchHistoryFromLocal() {
        historyUseCase.getHistoryFromLocal().onEach { result ->
            _state.update { it.copy(isLoading = true) }

            if (result.isNotEmpty()) {
                _state.update {
                    it.copy(
                        historyList = result.toMutableList(),
                        networkResult = NetworkResult.Success(result),
                        isLoading = false
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        networkResult = NetworkResult.Error(null, "Don't have history found."),
                        isLoading = false
                    )
                }
            }

        }.catch { e ->
            _state.update {
                it.copy(
                    networkResult = NetworkResult.Error(null, "Failed to load history."),
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)

    }

    private fun getExchangeRatesSafeCall(
        value: String,
        isSucceed: (success: Boolean) -> Unit = {}
    ) {
        _state.update { it.copy(networkResult = NetworkResult.Loading()) }

        getExchangeRateUseCase.invoke(value, application.applicationContext).onEach { data ->
            if (data != null) {
                _state.update {
                    it.copy(
                        currencyRate = data,
                        networkResult = NetworkResult.Success(data)
                    )
                }
                isSucceed(true)
            } else {
                _state.update {
                    it.copy(
                        networkResult = NetworkResult.Error(null, "Failed to fetch exchange rates.")
                    )
                }
            }
            isSucceed(false)
        }.catch { e ->
            _state.update {
                it.copy(
                    networkResult = NetworkResult.Error(null, "Network error: ${e.message}")
                )
            }
            isSucceed(false)
        }
            .launchIn(viewModelScope)
    }

    private fun getOutputString(value: Double): String {
        return "%.2f".format(value)
    }

    private fun getToValue(currencyCode: String, rates: ConversionRates?): Double {
        return when (currencyCode) {
            "AUD" -> rates?.aUD ?: 0.0
            "BRL" -> rates?.bRL ?: 0.0
            "BGN" -> rates?.bGN ?: 0.0
            "CAD" -> rates?.cAD ?: 0.0
            "CNY" -> rates?.cNY ?: 0.0
            "HRK" -> rates?.hRK ?: 0.0
            "CZK" -> rates?.cZK ?: 0.0
            "DKK" -> rates?.dKK ?: 0.0
            "EUR" -> rates?.eUR ?: 0.0
            "GBP" -> rates?.gBP ?: 0.0
            "HKD" -> rates?.hKD ?: 0.0
            "HUF" -> rates?.hUF ?: 0.0
            "ISK" -> rates?.iSK ?: 0.0
            "INR" -> rates?.iNR ?: 0.0
            "IDR" -> rates?.iDR ?: 0.0
            "ILS" -> rates?.iLS ?: 0.0
            "JPY" -> rates?.jPY ?: 0.0
            "KRW" -> rates?.kRW ?: 0.0
            "MYR" -> rates?.mYR ?: 0.0
            "MXN" -> rates?.mXN ?: 0.0
            "NZD" -> rates?.nZD ?: 0.0
            "NOK" -> rates?.nOK ?: 0.0
            "PHP" -> rates?.pHP ?: 0.0
            "PLN" -> rates?.pLN ?: 0.0
            "RON" -> rates?.rON ?: 0.0
            "RUB" -> rates?.rUB ?: 0.0
            "SGD" -> rates?.sGD ?: 0.0
            "ZAR" -> rates?.zAR ?: 0.0
            "SEK" -> rates?.sEK ?: 0.0
            "CHF" -> rates?.cHF ?: 0.0
            "THB" -> rates?.tHB ?: 0.0
            "TRY" -> rates?.tRY ?: 0.0
            "USD" -> rates?.uSD ?: 0.0
            "VND" -> rates?.vND ?: 0.0
            else -> 0.00
        }
    }

}