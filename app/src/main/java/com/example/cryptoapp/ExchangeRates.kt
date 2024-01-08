package com.example.cryptoapp

import com.google.gson.annotations.SerializedName

data class ExchangeRates(
    @SerializedName("success") val success: Boolean,
    @SerializedName("terms") val terms: String,
    @SerializedName("privacy") val privacy: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("target") val target: String,
    @SerializedName("rates") val rates: Map<String, Double>
)
