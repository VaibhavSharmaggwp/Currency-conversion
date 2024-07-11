package com.example.coinswap.main_screen

import android.text.Selection
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coinswap.domain.model.repository.CurrencyRepository
import com.example.coinswap.domain.model.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject
@HiltViewModel
class MainScreeViewModel @Inject constructor(
    private val repository: CurrencyRepository
): ViewModel() {

    var state by mutableStateOf(ManiScreenState())

    init {
        getCurrencyRatesList()
    }

    fun onEvents(events: MainScreenEvents){
        when(events){
            is MainScreenEvents.BottomSheetItemClicked -> {
                if(state.selection == SelectionState.FROM){
                    state = state.copy(fromCurrencyCode = events.values)
                }else if(state.selection == SelectionState.TO){
                    state = state.copy(toCurrencyCode = events.values)
                }
                updateCurrencyValue("")
            }
            MainScreenEvents.FromCurrencySelect -> {
                state = state.copy(selection = SelectionState.FROM)
            }
            is MainScreenEvents.NumberButtonClicked -> {
                updateCurrencyValue(value = events.values)
            }
            MainScreenEvents.SwapIconClicked -> {
                state = state.copy(fromCurrencyCode = state.toCurrencyCode,
                    fromCurrencyValue = state.toCurrencyValue,
                    toCurrencyCode = state.fromCurrencyCode,
                    toCurrencyValue = state.fromCurrencyValue
                    )
            }
            MainScreenEvents.ToCurrencySelect -> {
                state = state.copy(selection = SelectionState.TO)
            }
        }
    }

    private fun getCurrencyRatesList() {
        viewModelScope.launch {
            repository
                .getCurrencyRatesList()
                .collectLatest { result ->
                    state = when (result) {
                        is Resource.Error -> {
                            state.copy(
                                currencyRate = emptyMap(),
                                error = result.message // Set the error message here
                            )
                        }
                        is Resource.Success -> {
                            state.copy(
                                currencyRate = result.data?.associate { it.code to it } ?: emptyMap(),
                                error = null // Set error to null on success
                            )
                        }
                    }
                }
        }
    }

    private fun updateCurrencyValue(value: String){
        val currentCurrencyValue = when(state.selection){
            SelectionState.FROM -> state.fromCurrencyValue
            SelectionState.TO -> state.toCurrencyValue
        }
        val fromCurrencyRate = state.currencyRate[state.fromCurrencyCode]?.rate?: 0.0
        val toCurrencyRate = state.currencyRate[state.toCurrencyCode]?.rate?: 0.0

        val updatedCurrencyValue = when(value){
            "C" -> "0.00"
            else -> if(currentCurrencyValue == "0.00") value else currentCurrencyValue + value
        }

        val numberFormat = DecimalFormat("#.00")
        when(state.selection){
            SelectionState.FROM -> {
                val fromValue = updatedCurrencyValue.toDoubleOrNull()?: 0.0
                val toValue = fromValue / fromCurrencyRate * toCurrencyRate
                state = state.copy(
                    fromCurrencyValue = updatedCurrencyValue,
                    toCurrencyValue = numberFormat.format(toValue)

                )
            }
            SelectionState.TO -> {
                val toValue = updatedCurrencyValue.toDoubleOrNull()?: 0.0
                val fromValue = toValue / toCurrencyRate * fromCurrencyRate
                state = state.copy(
                    toCurrencyValue = updatedCurrencyValue,
                    fromCurrencyValue = numberFormat.format(fromValue)

                )
            }
        }

    }

}