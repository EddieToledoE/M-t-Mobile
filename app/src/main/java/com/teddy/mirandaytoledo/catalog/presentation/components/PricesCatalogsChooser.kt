package com.teddy.mirandaytoledo.catalog.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Rule
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Rule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teddy.mirandaytoledo.R

@Composable
fun PricesCatalogsChooser(
    modifier: Modifier = Modifier,
    onNavigateToProductTypes: () -> Unit = {},
    onNavigateToPricesRules: () -> Unit = {},
) {
    Column(modifier = modifier.fillMaxSize()) {
        CatalogChooser(
            modifier = Modifier.weight(weight = 1f),
            textResId = R.string.prices_catalog_chooser_product_types,
            icon = Icons.Outlined.Category,
            onClick = onNavigateToProductTypes
        )
        CatalogChooser(
            modifier = Modifier.weight(weight = 1f),
            textResId = R.string.prices_catalog_chooser_prices_rules,
            icon = Icons.AutoMirrored.Outlined.Rule,
            onClick = onNavigateToPricesRules
        )
    }
}
