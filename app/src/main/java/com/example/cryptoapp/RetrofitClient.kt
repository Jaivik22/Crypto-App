package com.example.cryptoapp

import com.google.android.gms.common.api.Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    private val BASE_URL = "http://api.coinlayer.com/"

//     Use lazy initialization for retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
//    val retrofit: Retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()

    // Use a companion object for singleton pattern
    companion object {
        private var mInstance: RetrofitClient? = null

        @Synchronized
        fun getInstance(): RetrofitClient {
            if (mInstance == null) {
                mInstance = RetrofitClient()
            }
            return mInstance!!
        }
    }

    // Provide a function to get the API interface
    fun getApi(): com.example.cryptoapp.Api {
        return retrofit.create(com.example.cryptoapp.Api::class.java)
    }
}

//data class Currency(
//    val symbol: String,
//    val name: String,
//    val fullName: String,
//    val maxSupply: Int,
//    val iconUrl: String
//)
