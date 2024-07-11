package com.example.coinswap.main_screen

import com.example.coinswap.domain.model.repository.CurrencyRate

data class ManiScreenState(
    val fromCurrencyCode: String = "INR",
    val toCurrencyCode: String = "USD",
    val fromCurrencyValue: String = "0.00",
    val toCurrencyValue: String = "0.00",
    val selection: SelectionState = SelectionState.FROM,
    val currencyRate: Map<String, CurrencyRate> = emptyMap(),
    val error: String? = null
)
enum class SelectionState{
    FROM,
    TO

}