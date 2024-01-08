package com.example.cryptoapp

import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.Api
import com.example.cryptoapp.ExchangeRates
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityUnitTest {



    private lateinit var apiMock: Api
    private lateinit var callMock: Call<ExchangeRates>
    private lateinit var mainActivity: MainActivity

    @Before
    fun setup() {
        apiMock = mock()
        callMock = mock()

        // Provide a default implementation for the callback (can be an empty lambda)
        val defaultCallback: (ExchangeRates) -> Unit = {}

        mainActivity = MainActivity(apiMock, defaultCallback)
    }


    @Test
    fun `fetchExchangeRates success`() {
        // Arrange
        val apiMock = mock<Api>()
        val callMock = mock<Call<ExchangeRates>>()

        whenever(apiMock.getExchangeRates(any())).thenReturn(callMock)

        val callbackCaptor = argumentCaptor<Callback<ExchangeRates>>()

        // Mock the callback function
        val mockCallback: (ExchangeRates) -> Unit = mock()

        // Create a test instance of MainActivity with the mock callback
        val mainActivity = MainActivity(apiMock, mockCallback)

        // Act
        mainActivity.fetchExchangeRates()

        // Verify
        verify(apiMock).getExchangeRates(any())
        verify(callMock).enqueue(callbackCaptor.capture())

        // Simulate a successful response
        val map: HashMap<String, Double> = mutableMapOf("name" to 34134.0) as HashMap<String, Double>
        val exchangeRates = ExchangeRates(true, "ABC", "Secure", 1641674401596, "yes", map)
        callbackCaptor.firstValue.onResponse(callMock, Response.success(exchangeRates))

        // Assert your expectations here
        verify(mockCallback).invoke(exchangeRates)
    }

    @Test
    fun `fetchExchangeRates failure`() {
        // Arrange
        val apiMock = mock<Api>()
        val callMock = mock<Call<ExchangeRates>>()

        whenever(apiMock.getExchangeRates(any())).thenReturn(callMock)

        val callbackCaptor = argumentCaptor<Callback<ExchangeRates>>()

        // Mock the callback function
        val mockErrorCallback: () -> Unit = mock()

        // Create a test instance of MainActivity with the mock error callback
//        val mainActivity = MainActivity(apiMock, errorCallback = mockErrorCallback)

        // Act
        mainActivity.fetchExchangeRates()

        // Verify
        verify(apiMock).getExchangeRates(any())
        verify(callMock).enqueue(callbackCaptor.capture())

        // Simulate a failure response
        callbackCaptor.firstValue.onFailure(callMock, mock())

        // Assert your expectations here
        verify(mockErrorCallback).invoke()
    }


}
