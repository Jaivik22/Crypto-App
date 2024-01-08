package com.example.cryptoapp

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale


class MainActivity() : AppCompatActivity() {
    var successCallback: ((ExchangeRates) -> Unit)? = null
    var errorCallback: (() -> Unit)? = null

    lateinit var cryptoAdapter: CryptoAdapter
    private val cryptoList: MutableList<CurrencyDetails> = mutableListOf()

    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val refreshInterval: Long = 3 * 60 * 1000 // 3 minutes in milliseconds
    var handler = Handler()

    lateinit var lastRefreshedTimeTextView: TextView
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    lateinit var progressDialog: ProgressDialog

    lateinit var warningtxt:TextView
    lateinit var retryBtn:ImageView

    lateinit var api: Api // Add this property



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)

        warningtxt = findViewById(R.id.warningtxt)
        retryBtn = findViewById(R.id.retryBtn)

        val rv: RecyclerView = findViewById(R.id.cryptoExchangeRV)
        val layoutManager = LinearLayoutManager(this)

        cryptoAdapter =CryptoAdapter(cryptoList);
        rv.layoutManager =layoutManager;
        rv.adapter = cryptoAdapter;

        lastRefreshedTimeTextView = findViewById(R.id.lastRefreshedTimeTextView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            fetchExchangeRates()
        }
        retryBtn.setOnClickListener {
            retryBtn.visibility = View.GONE
            warningtxt.visibility = View.GONE
            fetchDataList()
        }

        api = RetrofitClient.getInstance().getApi()

        fetchDataList()
        scheduleAutoRefresh()

    }

    // This function fatches list of data of all currencies
    fun fetchDataList() {
        progressDialog.show()
        lastRefreshedTimeTextView.visibility = View.VISIBLE

        val Api = RetrofitClient.getInstance().getApi()
        Api?.getCurrencies(apiKey = Constants.Constants.COINLAYER_API_KEY)?.enqueue(object : Callback<CurrenciesResponse> {
            override fun onResponse(call: Call<CurrenciesResponse>, response: Response<CurrenciesResponse>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val currenciesResponse = response.body()
                    currenciesResponse?.let {
                        val cryptoMap = it.crypto
                        if (cryptoMap != null) {
                            val currencies = cryptoMap.values.toList()
                            cryptoList.addAll(currencies)
                            cryptoAdapter.notifyDataSetChanged()
                            fetchExchangeRates()
                        }
                        else{
                            showErrorMessage("Failed to fetch currencies. Please try again.")
                            retryBtn.visibility = View.VISIBLE
                            warningtxt.visibility = View.VISIBLE
                            lastRefreshedTimeTextView.visibility = View.GONE
                        }
                    }
                }
                swipeRefreshLayout.isRefreshing = false
            }
            override fun onFailure(call: Call<CurrenciesResponse>, t: Throwable) {
                progressDialog.dismiss()
                showErrorMessage("Failed to fetch currencies. Please try again.")
                Log.d("Error", t.message.toString())
                swipeRefreshLayout.isRefreshing = false
                retryBtn.visibility = View.VISIBLE
                warningtxt.visibility = View.VISIBLE
                lastRefreshedTimeTextView.visibility = View.GONE
            }
        })
    }

    // This function helps to fetch live data
    fun fetchExchangeRates() {
        progressDialog.show()

        val api = RetrofitClient.getInstance().getApi()
        api?.getExchangeRates(apiKey = Constants.Constants.COINLAYER_API_KEY)?.enqueue(object : Callback<ExchangeRates> {
            override fun onResponse(call: Call<ExchangeRates>, response: Response<ExchangeRates>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    successCallback?.invoke(response.body()!!)
                    val exchangeRates = response.body()
                    Log.d("API Response", "getCurrencies Response: $exchangeRates")
                    exchangeRates?.let {
                        // Update rates for matching currencies
                        for (currency in cryptoList) {
                            val rate = exchangeRates.rates[currency.symbol];
                            if (rate != null) {
                                currency.rate = rate
                            }
                        }
                        cryptoAdapter.notifyDataSetChanged()
                        updateLastRefreshedTime()
                        showRefreshToast()
                    }
                }
                else{
                    showErrorMessage("Failed to fetch exchange rates. Please try again.")
                    errorCallback?.invoke()

                }
                swipeRefreshLayout.isRefreshing = false
            }
            override fun onFailure(call: Call<ExchangeRates>, t: Throwable) {
                errorCallback?.invoke()
                progressDialog.dismiss()
                showErrorMessage("Failed to fetch exchange rates. Please try again.")
                Log.d("Error", t.message.toString())
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    // This function updates last refreshed time
    fun updateLastRefreshedTime() {
        val currentTime = System.currentTimeMillis()
        val formattedTime = dateFormat.format(currentTime)
        lastRefreshedTimeTextView.text = "Last Refreshed: $formattedTime"
    }

    //This function fatches live data after every 3 minutes
    private fun scheduleAutoRefresh() {
        handler.postDelayed({
            // Refresh only the rates
            fetchExchangeRates()
            scheduleAutoRefresh()
        }, refreshInterval)
    }

    fun showRefreshToast() {
        Toast.makeText(this, "Data Refreshed", Toast.LENGTH_SHORT).show()
    }
    fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove any callbacks to prevent memory leaks
        handler.removeCallbacksAndMessages(null)
    }
}