package com.example.coinswap.main_screen

sealed class MainScreenEvents {
    object FromCurrencySelect: MainScreenEvents()
    object ToCurrencySelect: MainScreenEvents()
    object SwapIconClicked: MainScreenEvents()
    data class BottomSheetItemClicked(val values: String): MainScreenEvents()
    data class NumberButtonClicked(val values: String): MainScreenEvents()
}