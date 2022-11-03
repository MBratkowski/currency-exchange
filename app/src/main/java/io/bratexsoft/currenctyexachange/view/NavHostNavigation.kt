package io.bratexsoft.currenctyexachange.view

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.bratexsoft.currenctyexachange.domain.model.Currency
import io.bratexsoft.currenctyexachange.view.currency_exchange.CurrencyExchangeScreen
import io.bratexsoft.currenctyexachange.view.currency_exchange.CurrencyPickerMode
import io.bratexsoft.currenctyexachange.view.currency_search.CurrencyPickerScreen

@Composable
fun ExchangeCurrencyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = currencyExchangeRoute
) {
    var currencyPickModeState by remember { mutableStateOf<CurrencyPickerMode?>(null) }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = currencyExchangeRoute
        ) { backStackEntry ->
            val currencyPair = convertKeyResultToCurrency(backStackEntry, currencyPickModeState)
            CurrencyExchangeScreen(
                sellingCurrency = currencyPair.first,
                receivingCurrency = currencyPair.second
            ) {
                currencyPickModeState = it
                navController.navigate(currencyPickRoute)
            }
        }
        composable(
            route = currencyPickRoute
        ) {
            CurrencyPickerScreen { currency ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(currencyArg, currency)
                navController.popBackStack()
            }
        }
    }
}

fun convertKeyResultToCurrency(
    backStackEntry: NavBackStackEntry,
    currencyPickMode: CurrencyPickerMode?
): Pair<Currency?, Currency?> {
    val value = backStackEntry.savedStateHandle.get<String>(
        currencyArg
    ) ?: ""
    if (value.isEmpty()) {
        return Pair(null, null)
    }
    val currencyInformation = Currency.valueOf(value)
    return when (currencyPickMode) {
        CurrencyPickerMode.SELL -> Pair(currencyInformation, null)
        CurrencyPickerMode.RECEIVE -> Pair(null, currencyInformation)
        else -> Pair(null, null)
    }
}
