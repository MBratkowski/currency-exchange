package io.bratexsoft.currenctyexachange.view.currency_exchange

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.bratexsoft.currenctyexachange.NumberFormatter
import io.bratexsoft.currenctyexachange.R
import io.bratexsoft.currenctyexachange.domain.model.BalanceInformation
import io.bratexsoft.currenctyexachange.domain.model.Currency
import io.bratexsoft.currenctyexachange.domain.model.ExchangeEvent
import io.bratexsoft.currenctyexachange.view.CommissionFeeDialog
import io.bratexsoft.currenctyexachange.view.NoFundsToPayCommissionDialog
import io.bratexsoft.currenctyexachange.view.SpacerLarge

typealias AccountBalanceChanged = () -> List<Pair<Currency, Double>>
typealias BalanceInformationChanged = () -> BalanceInformation
typealias OnNavigateToCurrencySearch = (currencyPickMode: CurrencyPickerMode) -> Unit
typealias OnAmountValueChanged = (value: String) -> Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyExchangeScreen(
    viewModel: CurrencyExchangeViewModel = hiltViewModel(),
    sellingCurrency: Currency? = null,
    receivingCurrency: Currency? = null,
    onNavigateToCurrencySearch: OnNavigateToCurrencySearch
) {

    val uiEffect = viewModel.uiEffect.collectAsState(initial = null)
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.dispatchEvent(
            CurrencyExchangeViewEvent.UpdateCurrencies(
                sellingCurrency = sellingCurrency,
                receivingCurrency = receivingCurrency
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        }
    ) { it ->
        Column(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = dimensionResource(id = R.dimen.spacingLarge),
                end = dimensionResource(id = R.dimen.spacingLarge)
            )
        ) {
            Text(stringResource(id = R.string.currency_exchange_my_balances))
            CurrentAccountBalances(currentAccountBalances = { uiState.value.accountBalance.toList() })
            Spacer(modifier = Modifier.height(64.dp))
            Text(stringResource(id = R.string.currency_exchange_headline))
            SpacerLarge()
            ExchangeCurrency(
                exchangeCurrencyMode = ExchangeCurrencyMode.Sell,
                currencyInformation = { uiState.value.sellingBalanceInformation },
                onNavigateToCurrencySearch = { viewModel.dispatchEvent(CurrencyExchangeViewEvent.PickSellingCurrency) },
                onAmountValueChanged = { value ->
                    viewModel.dispatchEvent(
                        CurrencyExchangeViewEvent.SellingAmountUpdated(
                            value
                        )
                    )
                }
            )
            SpacerLarge()
            ExchangeCurrency(
                exchangeCurrencyMode = ExchangeCurrencyMode.Receive,
                currencyInformation = { uiState.value.receivingBalanceInformation },
                onNavigateToCurrencySearch = { viewModel.dispatchEvent(CurrencyExchangeViewEvent.PickReceiveCurrency) },
                onAmountValueChanged = { value ->
                    viewModel.dispatchEvent(
                        CurrencyExchangeViewEvent.ReceivingAmountUpdated(
                            value
                        )
                    )
                }
            )
            SpacerLarge()
            Button(
                enabled = uiState.value.exchangeButtonIsEnabled,
                onClick = {
                    viewModel.dispatchEvent(CurrencyExchangeViewEvent.Exchange)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.buttonHeight))
            ) {
                Text(stringResource(id = R.string.currency_exchange_button))
            }
            SpacerLarge()
            ExchangeHistory(exchangeHistoryList = uiState.value.exchangeHistoryList)
        }
    }

    uiEffect.value.let {
        when (it) {
            is CurrencyExchangeViewEffect.OpenCurrencyPicker -> {
                onNavigateToCurrencySearch(it.mode)
                viewModel.clearViewEffect()
            }
            is CurrencyExchangeViewEffect.ShowCommissionFeeDialog -> {
                CommissionFeeDialog(
                    sellingCurrency = it.sellingCurrency,
                    sellingAmount = it.sellingAmount,
                    receivingCurrency = it.receivingCurrency,
                    receivingAmount = it.receivingAmount,
                    commissionFeeAmount = it.commissionFeeAmount
                ) {
                    viewModel.clearViewEffect()
                }
            }
            is CurrencyExchangeViewEffect.NoFundsToPayCommission -> {
                NoFundsToPayCommissionDialog {
                    viewModel.clearViewEffect()
                }
            }
            else -> {}
        }
    }
}

@Composable
fun ExchangeHistory(exchangeHistoryList: List<ExchangeEvent>) {
    val decimalFormatter = remember { NumberFormatter.provideNumberFormatter() }

    Text(stringResource(id = R.string.currency_exchange_history))
    SpacerLarge()
    LazyColumn() {
        items(count = exchangeHistoryList.size) {
            Column(
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.spacingMedium),
                    bottom = dimensionResource(id = R.dimen.spacingLarge)
                )
            ) {
                val item = exchangeHistoryList[it]
                with(item) {
                    Text(
                        text = stringResource(
                            id = R.string.currency_exchange_history_item,
                            decimalFormatter.format(sellingAmount),
                            sellingCurrency.name,
                            decimalFormatter.format(receivingAmount),
                            receivingCurrency.name
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun CurrentAccountBalances(
    currentAccountBalances: AccountBalanceChanged
) {
    val decimalFormatter = remember { NumberFormatter.provideNumberFormatter() }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacingXLarge)))
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacingXLarge)),
        content = {
            items(items = currentAccountBalances(), itemContent = {
                Text(
                    "${decimalFormatter.format(it.second)} ${it.first}",
                    modifier = Modifier,
                    fontSize = 32.sp

                )
            })
        })
}

@Composable
fun ExchangeCurrency(
    exchangeCurrencyMode: ExchangeCurrencyMode,
    currencyInformation: BalanceInformationChanged,
    onNavigateToCurrencySearch: OnNavigateToCurrencySearch,
    onAmountValueChanged: OnAmountValueChanged
) {
    Column() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ExchangeIcon(exchangeCurrencyMode = exchangeCurrencyMode)
            Text(
                text = stringResource(id = exchangeCurrencyMode.textId),
                modifier = Modifier
                    .weight(0.25f)
                    .padding(start = dimensionResource(id = R.dimen.spacingLarge))
            )
            CurrencyTextField(
                modifier = Modifier
                    .weight(0.5f)
                    .width(dimensionResource(id = R.dimen.separatorPadding)),
                amount = currencyInformation().amount,
                onAmountValueChanged = onAmountValueChanged
            )
            CurrencyPicker(
                currencyPickMode = exchangeCurrencyMode.currencyPickMode,
                currencyInformation = currencyInformation,
                onNavigateToCurrencySearch = onNavigateToCurrencySearch,
                modifier = Modifier
                    .weight(0.25f)
                    .padding(start = dimensionResource(id = R.dimen.spacingLarge))
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacingMedium)))
        SeparatorLine()

    }
}

@Composable
fun ExchangeIcon(
    exchangeCurrencyMode: ExchangeCurrencyMode
) {
    Box(
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.buttonHeight))
            .clip(CircleShape)
            .background(exchangeCurrencyMode.color),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            exchangeCurrencyMode.icon,
            stringResource(id = exchangeCurrencyMode.textId),
            tint = Color.White
        )
    }
}

@Composable
fun CurrencyPicker(
    modifier: Modifier = Modifier,
    currencyPickMode: CurrencyPickerMode,
    currencyInformation: BalanceInformationChanged,
    onNavigateToCurrencySearch: OnNavigateToCurrencySearch
) {
    Box() {
        Row(
            modifier = modifier
                .width(dimensionResource(id = R.dimen.itemHeight))
                .clickable {
                    onNavigateToCurrencySearch(
                        currencyPickMode
                    )
                }, verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currencyInformation().currency.name
            )
            Icon(
                Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(id = R.string.currency_exchange_pick_currency)
            )
        }
    }
}

@Composable
fun CurrencyTextField(
    modifier: Modifier = Modifier,
    amount: Double,
    onAmountValueChanged: OnAmountValueChanged
) {
    val decimalFormatter = remember { NumberFormatter.provideNumberFormatter() }
    var currencyTextFieldFocused by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterEnd
    ) {
        AnimatedVisibility(
            visible = !currencyTextFieldFocused && amount == 0.0,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                textAlign = TextAlign.End,
                text = stringResource(id = R.string.currency_exchange_provide_value)
            )
        }
        BasicTextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    currencyTextFieldFocused = it.isFocused
                },
            value = if (amount == 0.0) "" else decimalFormatter.format(amount),
            onValueChange = {
                onAmountValueChanged(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
        )
    }
}

@Composable
fun SeparatorLine(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(start = dimensionResource(id = R.dimen.separatorPadding))
            .background(Color.LightGray)
    )
}

sealed class ExchangeCurrencyMode(
    val icon: ImageVector,
    val color: Color,
    @StringRes val textId: Int,
    val currencyPickMode: CurrencyPickerMode
) {
    object Sell : ExchangeCurrencyMode(
        icon = Icons.Filled.ArrowUpward,
        color = Color.Red,
        textId = R.string.currency_exchange_sell,
        currencyPickMode = CurrencyPickerMode.SELL
    )

    object Receive : ExchangeCurrencyMode(
        icon = Icons.Filled.ArrowDownward,
        color = Color.Green,
        textId = R.string.currency_exchange_receive,
        currencyPickMode = CurrencyPickerMode.RECEIVE
    )
}