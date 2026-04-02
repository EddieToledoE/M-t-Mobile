@file:OptIn(ExperimentalMaterial3Api::class)

package com.teddy.mirandaytoledo.catalog.prices.pricerules.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.Finish
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.Size
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.PriceRule
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductType
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackDialog
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackType
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackUi
import com.teddy.mirandaytoledo.core.presentation.util.toString
import org.koin.androidx.compose.koinViewModel
import java.util.Locale
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun PriceRulesCatalogScreen(
    modifier: Modifier = Modifier,
    viewModel: PriceRulesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showFormDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedRule by remember { mutableStateOf<PriceRule?>(null) }
    var feedback by remember { mutableStateOf<TimedFeedbackUi?>(null) }
    var awaitingMutationResult by remember { mutableStateOf(false) }

    var productTypeIdInput by remember { mutableIntStateOf(0) }
    var finishIdInput by remember { mutableIntStateOf(0) }
    var sizeIdInput by remember { mutableIntStateOf(0) }
    var priceInput by remember { mutableStateOf("") }

    fun resetForm() {
        selectedRule = null
        productTypeIdInput = 0
        finishIdInput = 0
        sizeIdInput = 0
        priceInput = ""
    }

    LaunchedEffect(uiState.isLoading, uiState.error, uiState.priceRules, awaitingMutationResult) {
        if (!awaitingMutationResult || uiState.isLoading) return@LaunchedEffect

        when {
            uiState.error != null -> {
                feedback = TimedFeedbackUi(
                    type = TimedFeedbackType.Error,
                    title = context.getString(R.string.feedback_error_title),
                    message = uiState.error!!.toString(context)
                )
                awaitingMutationResult = false
            }

            else -> {
                feedback = TimedFeedbackUi(
                    type = TimedFeedbackType.Success,
                    title = context.getString(R.string.feedback_success_title),
                    message = context.getString(R.string.feedback_success_message)
                )
                awaitingMutationResult = false
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.price_rules_error),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { viewModel.loadAll() }) {
                            Text(text = stringResource(R.string.retry))
                        }
                    }
                }
            }

            uiState.priceRules.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.price_rules_empty_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.price_rules_empty_message),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 240.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column(
                            modifier = Modifier.padding(vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.price_rules_title),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.price_rules_supporting_text),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    items(items = uiState.priceRules, key = { it.id }) { priceRule ->
                        PriceRuleItem(
                            priceRule = priceRule,
                            onEdit = {
                                selectedRule = priceRule
                                productTypeIdInput = priceRule.productTypeId
                                finishIdInput = priceRule.finishId
                                sizeIdInput = priceRule.sizeId
                                priceInput = formatPriceForInput(priceRule.price)
                                showFormDialog = true
                            },
                            onDelete = {
                                selectedRule = priceRule
                                showDeleteDialog = true
                            }
                        )
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(modifier = Modifier.height(88.dp))
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                resetForm()
                showFormDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.price_rules_add)
            )
        }
    }

    if (showFormDialog) {
        PriceRuleFormDialog(
            uiState = uiState,
            selectedRule = selectedRule,
            productTypeId = productTypeIdInput,
            onProductTypeChange = { productTypeIdInput = it },
            finishId = finishIdInput,
            onFinishChange = { finishIdInput = it },
            sizeId = sizeIdInput,
            onSizeChange = { sizeIdInput = it },
            price = priceInput,
            onPriceChange = { priceInput = it },
            onDismiss = { showFormDialog = false },
            onConfirm = { parsedPrice ->
                val productTypeId = productTypeIdInput
                val finishId = finishIdInput
                val sizeId = sizeIdInput

                if (selectedRule != null) {
                    awaitingMutationResult = true
                    viewModel.updatePriceRule(
                        id = selectedRule!!.id,
                        productTypeId = productTypeId,
                        finishId = finishId,
                        sizeId = sizeId,
                        price = parsedPrice
                    )
                } else {
                    awaitingMutationResult = true
                    viewModel.createPriceRule(
                        productTypeId = productTypeId,
                        finishId = finishId,
                        sizeId = sizeId,
                        price = parsedPrice
                    )
                }
                showFormDialog = false
            }
        )
    }

    if (showDeleteDialog && selectedRule != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = stringResource(R.string.price_rules_delete_dialog_title)) },
            text = {
                Text(
                    text = stringResource(
                        R.string.price_rules_delete_dialog_message,
                        selectedRule!!.productTypeName,
                        selectedRule!!.finishName,
                        selectedRule!!.sizeName
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        awaitingMutationResult = true
                        viewModel.deletePriceRule(selectedRule!!.id)
                        showDeleteDialog = false
                        selectedRule = null
                    }
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }

    TimedFeedbackDialog(
        feedback = feedback,
        onDismiss = { feedback = null }
    )
}

@Composable
private fun PriceRuleFormDialog(
    uiState: PriceRulesUiState,
    selectedRule: PriceRule?,
    productTypeId: Int,
    onProductTypeChange: (Int) -> Unit,
    finishId: Int,
    onFinishChange: (Int) -> Unit,
    sizeId: Int,
    onSizeChange: (Int) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    val eligibleProductTypes = remember(uiState.productTypes) {
        uiState.productTypes.filter { it.requiresSize && it.requiresFinish }
    }
    val selectedProductType = eligibleProductTypes.firstOrNull { it.id == productTypeId }
    val availableSizes = remember(selectedProductType, uiState.sizes) {
        uiState.sizes.filter { size ->
            selectedProductType?.allowedSizeGroup?.equals(size.sizeGroup, ignoreCase = true) ?: false
        }
    }

    LaunchedEffect(eligibleProductTypes, productTypeId) {
        if (eligibleProductTypes.isNotEmpty() && eligibleProductTypes.none { it.id == productTypeId }) {
            onProductTypeChange(eligibleProductTypes.first().id)
        }
    }

    LaunchedEffect(uiState.finishes, finishId) {
        if (uiState.finishes.isNotEmpty() && uiState.finishes.none { it.id == finishId }) {
            onFinishChange(uiState.finishes.first().id)
        }
    }

    LaunchedEffect(availableSizes, sizeId) {
        if (availableSizes.isNotEmpty() && availableSizes.none { it.id == sizeId }) {
            onSizeChange(availableSizes.first().id)
        }
    }

    var productTypeExpanded by remember { mutableStateOf(false) }
    var finishExpanded by remember { mutableStateOf(false) }
    var sizeExpanded by remember { mutableStateOf(false) }

    val priceValue = price.toDoubleOrNull()
    val isValid = eligibleProductTypes.isNotEmpty() &&
        productTypeId > 0 &&
        finishId > 0 &&
        sizeId > 0 &&
        priceValue != null &&
        priceValue > 0.0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(
                    if (selectedRule != null) {
                        R.string.price_rules_edit_dialog_title
                    } else {
                        R.string.price_rules_create_dialog_title
                    }
                )
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (eligibleProductTypes.isEmpty()) {
                    Text(
                        text = stringResource(R.string.price_rules_no_product_types_available),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    ProductTypeDropdown(
                        items = eligibleProductTypes,
                        selectedId = productTypeId,
                        expanded = productTypeExpanded,
                        onExpandedChange = { productTypeExpanded = !productTypeExpanded },
                        onSelected = {
                            onProductTypeChange(it)
                            sizeExpanded = false
                        }
                    )

                    FinishDropdown(
                        items = uiState.finishes,
                        selectedId = finishId,
                        expanded = finishExpanded,
                        onExpandedChange = { finishExpanded = !finishExpanded },
                        onSelected = onFinishChange
                    )

                    SizeDropdown(
                        items = availableSizes,
                        selectedId = sizeId,
                        expanded = sizeExpanded,
                        onExpandedChange = { sizeExpanded = !sizeExpanded },
                        onSelected = onSizeChange
                    )

                    OutlinedTextField(
                        value = price,
                        onValueChange = onPriceChange,
                        label = { Text(stringResource(R.string.price_rules_price_label)) },
                        supportingText = {
                            Text(text = stringResource(R.string.price_rules_price_supporting))
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (priceValue != null) onConfirm(priceValue) },
                enabled = isValid
            ) {
                Text(text = stringResource(if (selectedRule != null) R.string.update else R.string.create))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun ProductTypeDropdown(
    items: List<ProductType>,
    selectedId: Int,
    expanded: Boolean,
    onExpandedChange: () -> Unit,
    onSelected: (Int) -> Unit
) {
    val selected = items.firstOrNull { it.id == selectedId }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange() }
    ) {
        OutlinedTextField(
            value = selected?.name.orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.price_rules_product_type_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        DropdownMenu(expanded = expanded, onDismissRequest = onExpandedChange) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name) },
                    onClick = {
                        onSelected(item.id)
                        onExpandedChange()
                    }
                )
            }
        }
    }
}

@Composable
private fun FinishDropdown(
    items: List<Finish>,
    selectedId: Int,
    expanded: Boolean,
    onExpandedChange: () -> Unit,
    onSelected: (Int) -> Unit
) {
    val selected = items.firstOrNull { it.id == selectedId }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange() }
    ) {
        OutlinedTextField(
            value = selected?.name.orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.price_rules_finish_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        DropdownMenu(expanded = expanded, onDismissRequest = onExpandedChange) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name) },
                    onClick = {
                        onSelected(item.id)
                        onExpandedChange()
                    }
                )
            }
        }
    }
}

@Composable
private fun SizeDropdown(
    items: List<Size>,
    selectedId: Int,
    expanded: Boolean,
    onExpandedChange: () -> Unit,
    onSelected: (Int) -> Unit
) {
    val selected = items.firstOrNull { it.id == selectedId }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange() }
    ) {
        OutlinedTextField(
            value = selected?.name.orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.price_rules_size_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            supportingText = {
                if (selected != null) {
                    Text(
                        text = stringResource(
                            R.string.price_rules_size_group_value,
                            selected.sizeGroup.replaceFirstChar { it.uppercase() }
                        )
                    )
                }
            },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        DropdownMenu(expanded = expanded, onDismissRequest = onExpandedChange) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(
                                R.string.price_rules_size_option,
                                item.name,
                                item.sizeGroup.replaceFirstChar { it.uppercase() }
                            )
                        )
                    },
                    onClick = {
                        onSelected(item.id)
                        onExpandedChange()
                    }
                )
            }
        }
    }
}

@Composable
private fun PriceRuleItem(
    priceRule: PriceRule,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AttachMoney,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Column {
                        Text(
                            text = stringResource(R.string.price_rules_card_label),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = priceRule.productTypeName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = stringResource(R.string.price_rules_price_value, priceRule.price),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            RuleInfoRow(
                icon = Icons.Outlined.Sell,
                label = stringResource(R.string.price_rules_finish_value, priceRule.finishName)
            )
            RuleInfoRow(
                icon = Icons.Outlined.Straighten,
                label = stringResource(
                    R.string.price_rules_size_value,
                    priceRule.sizeName,
                    priceRule.sizeGroup.replaceFirstChar { it.uppercase() }
                )
            )

            Text(
                text = stringResource(R.string.price_rules_card_id, priceRule.id),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = stringResource(R.string.edit))
                }
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = stringResource(R.string.delete))
                }
            }
        }
    }
}

@Composable
private fun RuleInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun formatPriceForInput(value: Double): String {
    return String.format(Locale.US, "%.2f", value)
}
