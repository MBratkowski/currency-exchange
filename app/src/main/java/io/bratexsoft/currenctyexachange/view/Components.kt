package io.bratexsoft.currenctyexachange.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import io.bratexsoft.currenctyexachange.R

@Composable
fun SpacerLarge(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.spacingLarge)))
}