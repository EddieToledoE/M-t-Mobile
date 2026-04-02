@file:OptIn(ExperimentalMaterial3Api::class)

package com.teddy.mirandaytoledo.catalog.prices.producttypes.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Palette
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductType
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackDialog
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackType
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackUi
import com.teddy.mirandaytoledo.core.presentation.util.toString
import org.koin.androidx.compose.koinViewModel

private val sizeGroupOptions = listOf("package", "frame")

@Composable
fun ProductTypesCatalogScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductTypesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showFormDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedProductType by remember { mutableStateOf<ProductType?>(null) }
    var feedback by remember { mutableStateOf<TimedFeedbackUi?>(null) }
    var awaitingMutationResult by remember { mutableStateOf(false) }

    var nameInput by remember { mutableStateOf("") }
    var requiresSizeInput by remember { mutableStateOf(true) }
    var requiresFinishInput by remember { mutableStateOf(true) }
    var requiresFrameModelInput by remember { mutableStateOf(false) }
    var requiresColorInput by remember { mutableStateOf(false) }
    var allowedSizeGroupInput by remember { mutableStateOf(sizeGroupOptions.first()) }

    fun resetForm() {
        selectedProductType = null
        nameInput = ""
        requiresSizeInput = true
        requiresFinishInput = true
        requiresFrameModelInput = false
        requiresColorInput = false
        allowedSizeGroupInput = sizeGroupOptions.first()
    }

    LaunchedEffect(uiState, awaitingMutationResult) {
        if (!awaitingMutationResult) return@LaunchedEffect

        when (val state = uiState) {
            is ProductTypesUiState.Success -> {
                feedback = TimedFeedbackUi(
                    type = TimedFeedbackType.Success,
                    title = context.getString(R.string.feedback_success_title),
                    message = context.getString(R.string.feedback_success_message)
                )
                awaitingMutationResult = false
            }

            is ProductTypesUiState.Error -> {
                feedback = TimedFeedbackUi(
                    type = TimedFeedbackType.Error,
                    title = context.getString(R.string.feedback_error_title),
                    message = state.error.toString(context)
                )
                awaitingMutationResult = false
            }

            is ProductTypesUiState.Loading -> Unit
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is ProductTypesUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ProductTypesUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.product_types_error),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { viewModel.loadProductTypes() }) {
                            Text(text = stringResource(R.string.retry))
                        }
                    }
                }
            }

            is ProductTypesUiState.Success -> {
                if (state.productTypes.isEmpty()) {
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
                                text = stringResource(R.string.product_types_empty_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.product_types_empty_message),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 220.dp),
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
                                    text = stringResource(R.string.product_types_title),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = stringResource(R.string.product_types_supporting_text),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        items(items = state.productTypes, key = { it.id }) { productType ->
                            ProductTypeItem(
                                productType = productType,
                                onEdit = {
                                    selectedProductType = productType
                                    nameInput = productType.name
                                    requiresSizeInput = productType.requiresSize
                                    requiresFinishInput = productType.requiresFinish
                                    requiresFrameModelInput = productType.requiresFrameModel
                                    requiresColorInput = productType.requiresColor
                                    allowedSizeGroupInput =
                                        productType.allowedSizeGroup ?: sizeGroupOptions.first()
                                    showFormDialog = true
                                },
                                onDelete = {
                                    selectedProductType = productType
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
                contentDescription = stringResource(R.string.product_types_add)
            )
        }
    }

    if (showFormDialog) {
        val isEditing = selectedProductType != null
        var sizeGroupExpanded by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showFormDialog = false },
            title = {
                Text(
                    text = stringResource(
                        if (isEditing) R.string.product_types_edit_dialog_title
                        else R.string.product_types_create_dialog_title
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text(stringResource(R.string.product_types_name_label)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    RequirementToggle(
                        title = stringResource(R.string.product_types_requires_size_label),
                        supportingText = stringResource(R.string.product_types_requires_size_supporting),
                        checked = requiresSizeInput,
                        onCheckedChange = { isChecked ->
                            requiresSizeInput = isChecked
                            if (!isChecked) {
                                allowedSizeGroupInput = sizeGroupOptions.first()
                            }
                        }
                    )

                    if (requiresSizeInput) {
                        ExposedDropdownMenuBox(
                            expanded = sizeGroupExpanded,
                            onExpandedChange = { sizeGroupExpanded = !sizeGroupExpanded }
                        ) {
                            OutlinedTextField(
                                value = allowedSizeGroupInput.replaceFirstChar { it.uppercase() },
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(stringResource(R.string.product_types_size_group_label)) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = sizeGroupExpanded)
                                },
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )

                            DropdownMenu(
                                expanded = sizeGroupExpanded,
                                onDismissRequest = { sizeGroupExpanded = false }
                            ) {
                                sizeGroupOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option.replaceFirstChar { it.uppercase() }) },
                                        onClick = {
                                            allowedSizeGroupInput = option
                                            sizeGroupExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    RequirementToggle(
                        title = stringResource(R.string.product_types_requires_finish_label),
                        supportingText = stringResource(R.string.product_types_requires_finish_supporting),
                        checked = requiresFinishInput,
                        onCheckedChange = { isChecked ->
                            requiresFinishInput = isChecked
                            if (!isChecked) {
                                requiresFrameModelInput = false
                                requiresColorInput = false
                            }
                        }
                    )

                    RequirementToggle(
                        title = stringResource(R.string.product_types_requires_model_label),
                        supportingText = stringResource(R.string.product_types_requires_model_supporting),
                        checked = requiresFrameModelInput,
                        onCheckedChange = { isChecked ->
                            requiresFrameModelInput = isChecked
                            if (isChecked) {
                                requiresFinishInput = true
                            } else {
                                requiresColorInput = false
                            }
                        }
                    )

                    RequirementToggle(
                        title = stringResource(R.string.product_types_requires_color_label),
                        supportingText = stringResource(R.string.product_types_requires_color_supporting),
                        checked = requiresColorInput,
                        onCheckedChange = { isChecked ->
                            requiresColorInput = isChecked
                            if (isChecked) {
                                requiresFrameModelInput = true
                                requiresFinishInput = true
                            }
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (nameInput.isBlank()) return@TextButton

                        val allowedSizeGroup = if (requiresSizeInput) allowedSizeGroupInput else null

                        if (isEditing) {
                            val productType = selectedProductType ?: return@TextButton
                            awaitingMutationResult = true
                            viewModel.updateProductType(
                                id = productType.id,
                                name = nameInput,
                                requiresSize = requiresSizeInput,
                                requiresFinish = requiresFinishInput,
                                requiresFrameModel = requiresFrameModelInput,
                                requiresColor = requiresColorInput,
                                allowedSizeGroup = allowedSizeGroup
                            )
                        } else {
                            awaitingMutationResult = true
                            viewModel.createProductType(
                                name = nameInput,
                                requiresSize = requiresSizeInput,
                                requiresFinish = requiresFinishInput,
                                requiresFrameModel = requiresFrameModelInput,
                                requiresColor = requiresColorInput,
                                allowedSizeGroup = allowedSizeGroup
                            )
                        }

                        showFormDialog = false
                    }
                ) {
                    Text(text = stringResource(if (isEditing) R.string.update else R.string.create))
                }
            },
            dismissButton = {
                TextButton(onClick = { showFormDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }

    if (showDeleteDialog && selectedProductType != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = stringResource(R.string.product_types_delete_dialog_title)) },
            text = {
                Text(
                    text = stringResource(
                        R.string.product_types_delete_dialog_message,
                        selectedProductType!!.name
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        awaitingMutationResult = true
                        viewModel.deleteProductType(id = selectedProductType!!.id)
                        showDeleteDialog = false
                        selectedProductType = null
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
private fun ProductTypeItem(
    productType: ProductType,
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
                            imageVector = Icons.Outlined.Category,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Column {
                        Text(
                            text = stringResource(R.string.product_types_card_label),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = productType.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Surface(
                    color = if (productType.isActive) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = stringResource(
                            if (productType.isActive) R.string.status_active
                            else R.string.status_inactive
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (productType.isActive) {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }

            if (productType.allowedSizeGroup != null) {
                Text(
                    text = stringResource(
                        R.string.product_types_size_group_value,
                        productType.allowedSizeGroup.replaceFirstChar { it.uppercase() }
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RequirementChip(
                    icon = Icons.Outlined.Straighten,
                    text = stringResource(
                        if (productType.requiresSize) R.string.product_types_requires_size_enabled
                        else R.string.product_types_requires_size_disabled
                    ),
                    emphasized = productType.requiresSize
                )
                RequirementChip(
                    icon = Icons.Outlined.Sell,
                    text = stringResource(
                        if (productType.requiresFinish) R.string.product_types_requires_finish_enabled
                        else R.string.product_types_requires_finish_disabled
                    ),
                    emphasized = productType.requiresFinish
                )
                RequirementChip(
                    icon = Icons.Outlined.Style,
                    text = stringResource(
                        if (productType.requiresFrameModel) R.string.product_types_requires_model_enabled
                        else R.string.product_types_requires_model_disabled
                    ),
                    emphasized = productType.requiresFrameModel
                )
                RequirementChip(
                    icon = Icons.Outlined.Palette,
                    text = stringResource(
                        if (productType.requiresColor) R.string.product_types_requires_color_enabled
                        else R.string.product_types_requires_color_disabled
                    ),
                    emphasized = productType.requiresColor
                )
            }

            Text(
                text = stringResource(R.string.product_types_card_id, productType.id),
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
private fun RequirementToggle(
    title: String,
    supportingText: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = supportingText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun RequirementChip(
    icon: ImageVector,
    text: String,
    emphasized: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = if (emphasized) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (emphasized) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = if (emphasized) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
