package com.example.cryptoapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
interface Api {
    @GET("list")
    fun getCurrencies(@Query("access_key") apiKey: String): Call<CurrenciesResponse>

    @GET("live")
    fun getExchangeRates(@Query("access_key") apiKey: String): Call<ExchangeRates>



}