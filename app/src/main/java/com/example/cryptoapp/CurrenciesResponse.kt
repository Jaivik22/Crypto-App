package com.example.cryptoapp

import com.google.gson.annotations.SerializedName

data class CurrenciesResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("crypto") val crypto: Map<String, CurrencyDetails>
)
