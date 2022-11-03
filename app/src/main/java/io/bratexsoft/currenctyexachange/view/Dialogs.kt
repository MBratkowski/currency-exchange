package io.bratexsoft.currenctyexachange.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import io.bratexsoft.currenctyexachange.NumberFormatter
import io.bratexsoft.currenctyexachange.R
import io.bratexsoft.currenctyexachange.domain.model.Currency

@Composable
fun CommissionFeeDialog(
    sellingCurrency: Currency,
    sellingAmount: Double,
    receivingCurrency: Currency,
    receivingAmount: Double,
    commissionFeeAmount: Double,
    onClick: () -> Unit
) {

    val decimalFormatter = remember { NumberFormatter.provideNumberFormatter() }
    AlertDialog(
        onDismissRequest = onClick,
        title = {
            Text(text = stringResource(id = R.string.currency_converted_title))
        },
        text = {
            Text(
                text = if (!commissionFeeAmount.equals(0.0)) {
                    stringResource(
                        id = R.string.currency_converted_message,
                        decimalFormatter.format(sellingAmount),
                        sellingCurrency.name,
                        decimalFormatter.format(receivingAmount),
                        receivingCurrency.name,
                        decimalFormatter.format(commissionFeeAmount),
                        sellingCurrency.name
                    )
                } else {
                    stringResource(
                        id = R.string.currency_converted_message_no_fee,
                        decimalFormatter.format(sellingAmount),
                        sellingCurrency.name,
                        decimalFormatter.format(receivingAmount),
                        receivingCurrency.name
                    )
                }
            )
        },
        confirmButton = {
            Button(
                onClick = onClick
            ) {
                Text(stringResource(id = android.R.string.ok))
            }
        }
    )
}

@Composable
fun NoFundsToPayCommissionDialog(
    onClick: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onClick,
        title = {
            Text(text = stringResource(id = R.string.no_funds_title))
        },
        text = {
            Text(
                text = stringResource(id = R.string.no_funds_message)
            )
        },
        confirmButton = {
            Button(
                onClick = onClick
            ) {
                Text(stringResource(id = android.R.string.ok))
            }
        }
    )
}