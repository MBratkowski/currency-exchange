package io.bratexsoft.currenctyexachange.view.currency_exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bratexsoft.currenctyexachange.ExchangeCurrencyManager
import io.bratexsoft.currenctyexachange.domain.error.BalanceIsNegativeException
import io.bratexsoft.currenctyexachange.domain.model.Currency
import io.bratexsoft.currenctyexachange.domain.usecase.ExchangeCurrencyUseCase
import io.bratexsoft.currenctyexachange.domain.usecase.GetCurrentBalancesUseCase
import io.bratexsoft.currenctyexachange.domain.usecase.GetHistoryExchangeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CurrencyExchangeViewModel @Inject constructor(
    private val exchangeCurrencyManager: ExchangeCurrencyManager,
    private val getCurrentBalanceUseCase: GetCurrentBalancesUseCase,
    private val exchangeCurrencyUseCase: ExchangeCurrencyUseCase,
    private val getHistoryExchangeUseCase: GetHistoryExchangeUseCase
) : ViewModel() {

    private val _uiEffect = MutableSharedFlow<CurrencyExchangeViewEffect?>()
    val uiEffect: SharedFlow<CurrencyExchangeViewEffect?> = _uiEffect.asSharedFlow()

    private val _uiState = MutableStateFlow(CurrencyExchangeState())
    val uiState: StateFlow<CurrencyExchangeState> = _uiState

    init {
        exchangeCurrencyManager.subscribeRates(
            viewModelScope,
            _uiState.value.sellingBalanceInformation.currency
        )
    }

    fun dispatchEvent(event: CurrencyExchangeViewEvent) {
        when (event) {
            is CurrencyExchangeViewEvent.UpdateCurrencies -> {
                updatePickedCurrency(
                    sellingCurrency = event.sellingCurrency,
                    receivingCurrency = event.receivingCurrency
                )
            }
            is CurrencyExchangeViewEvent.PickSellingCurrency -> pickCurrency(currencyPickMode = CurrencyPickerMode.SELL)
            is CurrencyExchangeViewEvent.PickReceiveCurrency -> pickCurrency(currencyPickMode = CurrencyPickerMode.RECEIVE)
            is CurrencyExchangeViewEvent.SellingAmountUpdated -> {
                val amountValue = getAmountValue(event.amount)
                setBalance(
                    newAmount = amountValue,
                    receiveAmount = exchangeCurrencyManager.calculateReceivingAmount(
                        sellAmount = amountValue,
                        currency = _uiState.value.receivingBalanceInformation.currency
                    ),
                    sellAmount = amountValue
                )
            }
            is CurrencyExchangeViewEvent.ReceivingAmountUpdated -> {
                val amountValue = getAmountValue(event.amount)
                setBalance(
                    newAmount = amountValue,
                    sellAmount = exchangeCurrencyManager.calculateSellingAmount(
                        receivingAmount = amountValue,
                        currency = _uiState.value.receivingBalanceInformation.currency
                    ),
                    receiveAmount = amountValue
                )
            }
            is CurrencyExchangeViewEvent.Exchange -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val commissionFee = exchangeCurrencyUseCase(
                            sellingCurrency = _uiState.value.sellingBalanceInformation.currency,
                            receivingCurrency = _uiState.value.receivingBalanceInformation.currency,
                            amount = _uiState.value.sellingBalanceInformation.amount,
                            rate = exchangeCurrencyManager.provideRatesForCurrency(_uiState.value.receivingBalanceInformation.currency)
                        )
                        getCurrentBalances()
                        _uiEffect.emit(
                            CurrencyExchangeViewEffect.ShowCommissionFeeDialog(
                                sellingCurrency = _uiState.value.sellingBalanceInformation.currency,
                                receivingCurrency = _uiState.value.receivingBalanceInformation.currency,
                                sellingAmount = _uiState.value.sellingBalanceInformation.amount,
                                receivingAmount = _uiState.value.receivingBalanceInformation.amount,
                                commissionFeeAmount = commissionFee.toDouble(),
                            )
                        )
                        resetBalances()
                    } catch (e: Throwable) {
                        when (e) {
                            is BalanceIsNegativeException -> _uiEffect.emit(
                                CurrencyExchangeViewEffect.NoFundsToPayCommission
                            )
                        }
                    }
                }
            }
        }
    }

    private fun pickCurrency(currencyPickMode: CurrencyPickerMode) {
        viewModelScope.launch {
            _uiEffect.emit(CurrencyExchangeViewEffect.OpenCurrencyPicker(currencyPickMode))
        }
    }

    private fun updatePickedCurrency(
        sellingCurrency: Currency? = null,
        receivingCurrency: Currency? = null
    ) {
        getCurrentBalances()
        val currentState: CurrencyExchangeState = _uiState.value
        _uiState.updateState {
            _uiState.value.copy(
                sellingBalanceInformation = currentState.sellingBalanceInformation.copy(
                    currency = sellingCurrency ?: currentState.sellingBalanceInformation.currency
                ),
                receivingBalanceInformation = currentState.receivingBalanceInformation.copy(
                    currency = receivingCurrency
                        ?: currentState.receivingBalanceInformation.currency,
                    amount = exchangeCurrencyManager.calculateReceivingAmount(
                        currentState.sellingBalanceInformation.amount,
                        currency = receivingCurrency
                            ?: currentState.receivingBalanceInformation.currency
                    )
                )
            )
        }
        if (sellingCurrency != null) {
            exchangeCurrencyManager.resubscribeRates(viewModelScope, sellingCurrency)
        }
    }

    private fun getCurrentBalances() {
        viewModelScope.launch(Dispatchers.IO) {
            val balances = getCurrentBalanceUseCase()
            val eventHistoryList = getHistoryExchangeUseCase()
            _uiState.updateState {
                _uiState.value.copy(
                    accountBalance = balances.toMutableMap(),
                    exchangeHistoryList = eventHistoryList
                )
            }
        }
    }

    fun clearViewEffect() {
        viewModelScope.launch { _uiEffect.emit(null) }
    }

    private fun setBalance(
        newAmount: Double,
        sellAmount: Double,
        receiveAmount: Double
    ) {
        if (newAmount == 0.0) {
            resetBalances()
        } else {
            setBalancesState(
                sellAmount = sellAmount,
                receivingAmount = receiveAmount
            )
        }
    }

    private fun setBalancesState(sellAmount: Double, receivingAmount: Double) {
        _uiState.updateState {
            _uiState.value.copy(
                sellingBalanceInformation = _uiState.value.sellingBalanceInformation.copy(
                    amount = sellAmount
                ),
                receivingBalanceInformation = _uiState.value.receivingBalanceInformation.copy(
                    amount = receivingAmount
                ),
                exchangeButtonIsEnabled = _uiState.value.getBalanceOfSellingCurrency() >= sellAmount
            )
        }
    }

    private fun resetBalances() {
        _uiState.updateState {
            _uiState.value.copy(
                sellingBalanceInformation = _uiState.value.sellingBalanceInformation.copy(
                    amount = 0.0
                ),
                receivingBalanceInformation = _uiState.value.receivingBalanceInformation.copy(
                    amount = 0.0
                ),
                exchangeButtonIsEnabled = false
            )
        }
    }

    private fun getAmountValue(amount: String): Double {
        return if (amount.isEmpty()) 0.toDouble() else amount.toDouble()
    }
}

inline fun <T> MutableStateFlow<T>.updateState(state: () -> T) {
    this.value = state()
}
