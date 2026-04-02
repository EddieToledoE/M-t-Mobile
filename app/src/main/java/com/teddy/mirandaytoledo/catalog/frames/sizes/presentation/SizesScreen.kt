@file:OptIn(ExperimentalMaterial3Api::class)

package com.teddy.mirandaytoledo.catalog.frames.sizes.presentation

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.Straighten
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.Size
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackDialog
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackType
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackUi
import com.teddy.mirandaytoledo.core.presentation.util.toString
import org.koin.androidx.compose.koinViewModel

private val sizeGroupOptions = listOf("frame", "package")

@Composable
fun SizesScreen(
    modifier: Modifier = Modifier,
    viewModel: SizesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showFormDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedSize by remember { mutableStateOf<Size?>(null) }
    var feedback by remember { mutableStateOf<TimedFeedbackUi?>(null) }
    var awaitingMutationResult by remember { mutableStateOf(false) }

    var nameInput by remember { mutableStateOf("") }
    var sizeGroupInput by remember { mutableStateOf(sizeGroupOptions.first()) }
    var isActiveInput by remember { mutableStateOf(true) }

    LaunchedEffect(uiState, awaitingMutationResult) {
        if (!awaitingMutationResult) return@LaunchedEffect

        when (val state = uiState) {
            is SizesUiState.Success -> {
                feedback = TimedFeedbackUi(
                    type = TimedFeedbackType.Success,
                    title = context.getString(R.string.feedback_success_title),
                    message = context.getString(R.string.feedback_success_message)
                )
                awaitingMutationResult = false
            }

            is SizesUiState.Error -> {
                feedback = TimedFeedbackUi(
                    type = TimedFeedbackType.Error,
                    title = context.getString(R.string.feedback_error_title),
                    message = state.error.toString(context)
                )
                awaitingMutationResult = false
            }

            is SizesUiState.Loading -> Unit
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (val state = uiState) {
            is SizesUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SizesUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.sizes_error),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { viewModel.loadSizes() }) {
                            Text(text = stringResource(R.string.retry))
                        }
                    }
                }
            }

            is SizesUiState.Success -> {
                if (state.sizes.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.sizes_empty_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.sizes_empty_message),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 180.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Text(
                                text = stringResource(R.string.sizes_title),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }

                        items(items = state.sizes, key = { it.id }) { size ->
                            SizeItem(
                                size = size,
                                onEdit = {
                                    selectedSize = size
                                    nameInput = size.name
                                    sizeGroupInput = size.sizeGroup
                                    isActiveInput = size.isActive
                                    showFormDialog = true
                                },
                                onDelete = {
                                    selectedSize = size
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
                selectedSize = null
                nameInput = ""
                sizeGroupInput = sizeGroupOptions.first()
                isActiveInput = true
                showFormDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.sizes_add)
            )
        }
    }

    if (showFormDialog) {
        val isEditing = selectedSize != null
        var sizeGroupExpanded by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showFormDialog = false },
            title = {
                Text(
                    text = stringResource(
                        if (isEditing) R.string.sizes_edit_dialog_title
                        else R.string.sizes_create_dialog_title
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text(stringResource(R.string.sizes_name_label)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = sizeGroupExpanded,
                        onExpandedChange = { sizeGroupExpanded = !sizeGroupExpanded }
                    ) {
                        OutlinedTextField(
                            value = sizeGroupInput.replaceFirstChar { it.uppercase() },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.sizes_group_label)) },
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
                                        sizeGroupInput = option
                                        sizeGroupExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    if (isEditing) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.sizes_active_label),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = stringResource(R.string.sizes_active_supporting_text),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Switch(
                                checked = isActiveInput,
                                onCheckedChange = { isActiveInput = it }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (nameInput.isBlank()) return@TextButton

                        if (isEditing) {
                            val size = selectedSize ?: return@TextButton
                            awaitingMutationResult = true
                            viewModel.updateSize(
                                id = size.id,
                                name = nameInput,
                                sizeGroup = sizeGroupInput,
                                isActive = isActiveInput
                            )
                        } else {
                            awaitingMutationResult = true
                            viewModel.createSize(
                                name = nameInput,
                                sizeGroup = sizeGroupInput
                            )
                        }

                        showFormDialog = false
                    }
                ) {
                    Text(
                        text = stringResource(
                            if (isEditing) R.string.update else R.string.create
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showFormDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }

    if (showDeleteDialog && selectedSize != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = stringResource(R.string.sizes_delete_dialog_title)) },
            text = {
                Text(
                    text = stringResource(
                        R.string.sizes_delete_dialog_message,
                        selectedSize!!.name
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        awaitingMutationResult = true
                        viewModel.deleteSize(id = selectedSize!!.id)
                        showDeleteDialog = false
                        selectedSize = null
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
private fun SizeItem(
    size: Size,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
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
                            imageVector = Icons.Outlined.Straighten,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Column {
                        Text(
                            text = stringResource(R.string.sizes_card_label),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = size.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Surface(
                    color = if (size.isActive) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = stringResource(
                            if (size.isActive) R.string.status_active
                            else R.string.status_inactive
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (size.isActive) {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }

            Text(
                text = stringResource(
                    R.string.sizes_group_value,
                    size.sizeGroup.replaceFirstChar { it.uppercase() }
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = stringResource(R.string.sizes_card_id, size.id),
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
