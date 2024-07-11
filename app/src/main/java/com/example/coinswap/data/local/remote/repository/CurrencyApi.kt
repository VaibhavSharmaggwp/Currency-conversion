package com.example.coinswap.data.local.remote.repository

import com.example.coinswap.data.local.remote.repository.Dto.CurrencyDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("v1/latest")
    suspend fun getLatestRates(
        @Query("apikey") apiKey: String = API_KEY
    ): CurrencyDto
    companion object{
        const val API_KEY = "fca_live_J18z5n5FhFR9FsqIyJqlU5ZoVDjQ0KqHUzpUgfFS"
        const val BASE_URL = "https://api.freecurrencyapi.com/"
    }
}