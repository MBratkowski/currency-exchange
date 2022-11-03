package io.bratexsoft.currenctyexachange.view.currency_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bratexsoft.currenctyexachange.domain.usecase.GetCurrenciesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyPickerViewModel @Inject constructor(
    private val getCurrencies: GetCurrenciesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyPickerState())
    val uiState: StateFlow<CurrencyPickerState> = _uiState

    fun dispatchEvent(event: CurrencyPickerEvent) {
        when (event) {
            is CurrencyPickerEvent.LoadCurrencies -> {
                loadCurrencies()
            }
        }
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            val currencies = getCurrencies()
            _uiState.value = _uiState.value.copy(currenciesSet = currencies)
        }
    }
}