@file:OptIn(ExperimentalMaterial3Api::class)

package com.teddy.mirandaytoledo.catalog.frames.restrictions.presentation

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Link
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.CatalogColor
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishColorRelation
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelation
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import org.koin.androidx.compose.koinViewModel

@Composable
fun FrameRestrictionsScreen(
    modifier: Modifier = Modifier,
    viewModel: FrameRestrictionsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showCombinationDialog by remember { mutableStateOf(false) }
    var showColorDialog by remember { mutableStateOf(false) }
    var combinationToDelete by remember { mutableStateOf<FrameModelFinishRelation?>(null) }
    var colorRestrictionToDelete by remember { mutableStateOf<FrameModelFinishColorRelation?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(top = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.frame_restrictions_title),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.frame_restrictions_supporting_text),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                uiState.error?.let { error ->
                    item {
                        ElevatedCard(
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = networkErrorMessage(error = error),
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                item {
                    SectionHeader(
                        title = stringResource(R.string.frame_restrictions_combinations_title),
                        subtitle = stringResource(R.string.frame_restrictions_combinations_subtitle),
                        actionLabel = null,
                        onActionClick = null
                    )
                }

                if (uiState.combinations.isEmpty()) {
                    item {
                        EmptyInfoCard(
                            title = stringResource(R.string.frame_restrictions_combinations_empty_title),
                            message = stringResource(R.string.frame_restrictions_combinations_empty_message)
                        )
                    }
                } else {
                    items(
                        items = uiState.combinations,
                        key = { "${it.frameModelId}-${it.finishId}" }
                    ) { combination ->
                        val selectedCombination = uiState.selectedCombination
                        CombinationCard(
                            combination = combination,
                            isSelected = selectedCombination?.frameModelId == combination.frameModelId &&
                                selectedCombination?.finishId == combination.finishId,
                            onSelect = { viewModel.selectCombination(combination) },
                            onDelete = { combinationToDelete = combination }
                        )
                    }
                }

                item {
                    SectionHeader(
                        title = stringResource(R.string.frame_restrictions_colors_title),
                        subtitle = uiState.selectedCombination?.let {
                            stringResource(
                                R.string.frame_restrictions_colors_selected_subtitle,
                                it.frameModelName,
                                it.finishName
                            )
                        } ?: stringResource(R.string.frame_restrictions_colors_unselected_subtitle),
                        actionLabel = if (uiState.selectedCombination != null) {
                            stringResource(R.string.frame_restrictions_add_color)
                        } else {
                            null
                        },
                        onActionClick = if (uiState.selectedCombination != null) {
                            { showColorDialog = true }
                        } else {
                            null
                        }
                    )
                }

                if (uiState.selectedCombination == null) {
                    item {
                        EmptyInfoCard(
                            title = stringResource(R.string.frame_restrictions_select_combination_title),
                            message = stringResource(R.string.frame_restrictions_select_combination_message)
                        )
                    }
                } else if (uiState.isColorSectionLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else if (uiState.allowedColors.isEmpty()) {
                    item {
                        EmptyInfoCard(
                            title = stringResource(R.string.frame_restrictions_colors_empty_title),
                            message = stringResource(R.string.frame_restrictions_colors_empty_message)
                        )
                    }
                } else {
                    items(
                        items = uiState.allowedColors,
                        key = { "${it.frameModelId}-${it.finishId}-${it.colorId}" }
                    ) { colorRelation ->
                        ColorRestrictionCard(
                            colorRelation = colorRelation,
                            onDelete = { colorRestrictionToDelete = colorRelation }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(88.dp))
                }
            }
        }

        FloatingActionButton(
            onClick = { showCombinationDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.frame_restrictions_add_combination)
            )
        }
    }

    if (showCombinationDialog) {
        CombinationDialog(
            modelOptions = uiState.frameModels,
            finishOptions = uiState.finishes,
            existingCombinations = uiState.combinations,
            onDismiss = { showCombinationDialog = false },
            onConfirm = { frameModelId, finishId ->
                viewModel.createCombination(frameModelId = frameModelId, finishId = finishId)
                showCombinationDialog = false
            }
        )
    }

    if (showColorDialog && uiState.selectedCombination != null) {
        ColorRestrictionDialog(
            availableColors = uiState.colors,
            assignedColors = uiState.allowedColors.map { it.colorId }.toSet(),
            selectedCombination = uiState.selectedCombination!!,
            onDismiss = { showColorDialog = false },
            onConfirm = { colorId ->
                viewModel.createColorRestriction(colorId = colorId)
                showColorDialog = false
            }
        )
    }

    if (combinationToDelete != null) {
        AlertDialog(
            onDismissRequest = { combinationToDelete = null },
            title = { Text(text = stringResource(R.string.frame_restrictions_delete_combination_title)) },
            text = {
                Text(
                    text = stringResource(
                        R.string.frame_restrictions_delete_combination_message,
                        combinationToDelete!!.frameModelName,
                        combinationToDelete!!.finishName
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteCombination(
                            frameModelId = combinationToDelete!!.frameModelId,
                            finishId = combinationToDelete!!.finishId
                        )
                        combinationToDelete = null
                    }
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { combinationToDelete = null }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }

    if (colorRestrictionToDelete != null) {
        AlertDialog(
            onDismissRequest = { colorRestrictionToDelete = null },
            title = { Text(text = stringResource(R.string.frame_restrictions_delete_color_title)) },
            text = {
                Text(
                    text = stringResource(
                        R.string.frame_restrictions_delete_color_message,
                        colorRestrictionToDelete!!.colorName
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteColorRestriction(colorId = colorRestrictionToDelete!!.colorId)
                        colorRestrictionToDelete = null
                    }
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { colorRestrictionToDelete = null }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    actionLabel: String?,
    onActionClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (actionLabel != null && onActionClick != null) {
            TextButton(onClick = onActionClick) {
                Text(text = actionLabel)
            }
        }
    }
}

@Composable
private fun EmptyInfoCard(
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CombinationCard(
    combination: FrameModelFinishRelation,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = MaterialTheme.shapes.large
            )
            .clickable(onClick = onSelect),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
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
                            imageVector = Icons.Outlined.Link,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = stringResource(R.string.frame_restrictions_combination_label),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = combination.frameModelName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(
                                R.string.frame_restrictions_finish_value,
                                combination.finishName
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (isSelected) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = stringResource(R.string.frame_restrictions_selected),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
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
private fun ColorRestrictionCard(
    colorRelation: FrameModelFinishColorRelation,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val swatchColor = colorFromHexOrNull(colorRelation.colorHex) ?: MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (swatchColor.luminance() > 0.5f) Color.Black else Color.White

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(swatchColor)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = colorRelation.colorHex ?: stringResource(R.string.catalog_colors_no_hex_short),
                    color = textColor,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = colorRelation.colorName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(
                        R.string.frame_restrictions_color_hex_value,
                        colorRelation.colorHex ?: stringResource(R.string.catalog_colors_not_set)
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(
                onClick = onDelete,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete)
                )
            }
        }
    }
}

@Composable
private fun CombinationDialog(
    modelOptions: List<com.teddy.mirandaytoledo.catalog.frames.models.domain.FrameModel>,
    finishOptions: List<com.teddy.mirandaytoledo.catalog.frames.finishes.domain.Finish>,
    existingCombinations: List<FrameModelFinishRelation>,
    onDismiss: () -> Unit,
    onConfirm: (frameModelId: Int, finishId: Int) -> Unit
) {
    var selectedModelId by remember { mutableIntStateOf(modelOptions.firstOrNull()?.id ?: 0) }
    var selectedFinishId by remember { mutableIntStateOf(finishOptions.firstOrNull()?.id ?: 0) }
    var modelExpanded by remember { mutableStateOf(false) }
    var finishExpanded by remember { mutableStateOf(false) }

    val selectedModel = modelOptions.firstOrNull { it.id == selectedModelId }
    val selectedFinish = finishOptions.firstOrNull { it.id == selectedFinishId }
    val alreadyExists = existingCombinations.any {
        it.frameModelId == selectedModelId && it.finishId == selectedFinishId
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.frame_restrictions_create_combination_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ExposedDropdownMenuBox(
                    expanded = modelExpanded,
                    onExpandedChange = { modelExpanded = !modelExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedModel?.name.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.frame_models_name_label)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )

                    DropdownMenu(
                        expanded = modelExpanded,
                        onDismissRequest = { modelExpanded = false }
                    ) {
                        modelOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.name) },
                                onClick = {
                                    selectedModelId = option.id
                                    modelExpanded = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = finishExpanded,
                    onExpandedChange = { finishExpanded = !finishExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedFinish?.name.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.finishes_name_label)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = finishExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )

                    DropdownMenu(
                        expanded = finishExpanded,
                        onDismissRequest = { finishExpanded = false }
                    ) {
                        finishOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.name) },
                                onClick = {
                                    selectedFinishId = option.id
                                    finishExpanded = false
                                }
                            )
                        }
                    }
                }

                if (alreadyExists) {
                    Text(
                        text = stringResource(R.string.frame_restrictions_combination_exists_message),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selectedModelId, selectedFinishId) },
                enabled = selectedModel != null && selectedFinish != null && !alreadyExists
            ) {
                Text(text = stringResource(R.string.create))
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
private fun ColorRestrictionDialog(
    availableColors: List<CatalogColor>,
    assignedColors: Set<Int>,
    selectedCombination: FrameModelFinishRelation,
    onDismiss: () -> Unit,
    onConfirm: (colorId: Int) -> Unit
) {
    val unassignedColors = availableColors.filterNot { it.id in assignedColors }
    var selectedColorId by remember { mutableIntStateOf(unassignedColors.firstOrNull()?.id ?: 0) }
    var colorExpanded by remember { mutableStateOf(false) }
    val selectedColor = unassignedColors.firstOrNull { it.id == selectedColorId }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.frame_restrictions_create_color_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.frame_restrictions_selected_combination_label),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = selectedCombination.frameModelName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(
                                R.string.frame_restrictions_finish_value,
                                selectedCombination.finishName
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (unassignedColors.isEmpty()) {
                    Text(
                        text = stringResource(R.string.frame_restrictions_all_colors_assigned),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    ExposedDropdownMenuBox(
                        expanded = colorExpanded,
                        onExpandedChange = { colorExpanded = !colorExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedColor?.name.orEmpty(),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.catalog_colors_name_label)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = colorExpanded) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )

                        DropdownMenu(
                            expanded = colorExpanded,
                            onDismissRequest = { colorExpanded = false }
                        ) {
                            unassignedColors.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.name) },
                                    onClick = {
                                        selectedColorId = option.id
                                        colorExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selectedColorId) },
                enabled = selectedColor != null
            ) {
                Text(text = stringResource(R.string.create))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}

private fun colorFromHexOrNull(hex: String?): Color? {
    if (hex.isNullOrBlank()) return null
    if (!Regex("^#[0-9A-Fa-f]{6}$").matches(hex)) return null

    return runCatching {
        Color(AndroidColor.parseColor(hex))
    }.getOrNull()
}

@Composable
private fun networkErrorMessage(error: NetworkError): String {
    return when (error) {
        NetworkError.REQUEST_TIMEOUT -> stringResource(R.string.error_request_timeout)
        NetworkError.TOO_MANY_REQUESTS -> stringResource(R.string.error_many_request)
        NetworkError.NO_INTERNET -> stringResource(R.string.error_no_internet)
        NetworkError.SERVER_ERROR -> stringResource(R.string.error_server_error)
        NetworkError.SERIALIZATION -> stringResource(R.string.error_serialization)
        NetworkError.UNKNOWN -> stringResource(R.string.error_unknown)
        NetworkError.INCORRECT_CREDENTIALS -> stringResource(R.string.error_incorrect_credential)
    }
}
