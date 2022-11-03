package io.bratexsoft.currenctyexachange.view.currency_search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import io.bratexsoft.currenctyexachange.R
import io.bratexsoft.currenctyexachange.domain.model.Currency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyPickerScreen(
    viewModel: CurrencyPickerViewModel = hiltViewModel(),
    onNavigateToCurrencyExchange: (currency: String) -> Unit
) {

    var searchedCurrencyTextField by remember { mutableStateOf(TextFieldValue("")) }
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.dispatchEvent(CurrencyPickerEvent.LoadCurrencies)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    stringResource(id = R.string.pick_currency_title),
                )
            },
        )
    }, content = { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TextField(modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.spacingLarge)),
                value = searchedCurrencyTextField,
                onValueChange = { searchedCurrencyTextField = it },
                maxLines = 1,
                label = { Text(stringResource(id = R.string.pick_currency_enter)) })
            CurrenciesList(
                currenciesList = uiState.value.currenciesSet.toList(),
                filterPhrase = searchedCurrencyTextField.text,
                onNavigateToCurrencyExchange = onNavigateToCurrencyExchange
            )
        }
    })
}

@Composable
fun CurrenciesList(
    currenciesList: List<Currency>,
    filterPhrase: String,
    onNavigateToCurrencyExchange: (currency: String) -> Unit,
) {

    val filteredCurrenciesList = mutableListOf<String>()

    currenciesList.forEach { currency ->
        if (currency.name.contains(filterPhrase, ignoreCase = true)) {
            filteredCurrenciesList.add(currency.name)
        }
    }
    if (filteredCurrenciesList.isNotEmpty()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacingMedium))
        ) {
            items(count = filteredCurrenciesList.size) {
                Box(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.itemHeight))
                        .clickable {
                            onNavigateToCurrencyExchange.invoke(
                                filteredCurrenciesList[it]
                            )
                        }, contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filteredCurrenciesList[it],
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = dimensionResource(id = R.dimen.spacingLarge))
                    )
                }
            }
        }
    }
}
