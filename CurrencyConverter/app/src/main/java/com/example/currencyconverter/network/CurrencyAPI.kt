package com.example.currencyconverter.network

import com.example.currencyconverter.models.CurrencyDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyAPI {
    @GET("latest/{path}")
    suspend fun getExchangeRates(@Path("path") path: String): Response<CurrencyDTO>
}