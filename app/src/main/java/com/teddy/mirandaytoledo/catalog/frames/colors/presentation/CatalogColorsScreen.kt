package com.teddy.mirandaytoledo.catalog.frames.colors.presentation

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.CatalogColor
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackDialog
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackType
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackUi
import com.teddy.mirandaytoledo.core.presentation.util.toString
import org.koin.androidx.compose.koinViewModel
import androidx.core.graphics.toColorInt

private val quickColorOptions = listOf(
    "#FFFFFF",
    "#000000",
    "#FFD700",
    "#C0C0C0",
    "#8B4513",
    "#FF0000",
    "#0000FF",
    "#008000"
)

@Composable
fun CatalogColorsScreen(
    modifier: Modifier = Modifier,
    viewModel: CatalogColorsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showFormDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf<CatalogColor?>(null) }
    var feedback by remember { mutableStateOf<TimedFeedbackUi?>(null) }
    var awaitingMutationResult by remember { mutableStateOf(false) }

    var nameInput by remember { mutableStateOf("") }
    var hexInput by remember { mutableStateOf("") }

    LaunchedEffect(uiState, awaitingMutationResult) {
        if (!awaitingMutationResult) return@LaunchedEffect

        when (val state = uiState) {
            is CatalogColorsUiState.Success -> {
                feedback = TimedFeedbackUi(
                    type = TimedFeedbackType.Success,
                    title = context.getString(R.string.feedback_success_title),
                    message = context.getString(R.string.feedback_success_message)
                )
                awaitingMutationResult = false
            }

            is CatalogColorsUiState.Error -> {
                feedback = TimedFeedbackUi(
                    type = TimedFeedbackType.Error,
                    title = context.getString(R.string.feedback_error_title),
                    message = state.error.toString(context)
                )
                awaitingMutationResult = false
            }

            is CatalogColorsUiState.Loading -> Unit
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (val state = uiState) {
            is CatalogColorsUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is CatalogColorsUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.catalog_colors_error),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { viewModel.loadColors() }) {
                            Text(text = stringResource(R.string.retry))
                        }
                    }
                }
            }

            is CatalogColorsUiState.Success -> {
                if (state.colors.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.catalog_colors_empty_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.catalog_colors_empty_message),
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
                                text = stringResource(R.string.catalog_colors_title),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }

                        items(items = state.colors, key = { it.id }) { colorItem ->
                            CatalogColorItem(
                                colorItem = colorItem,
                                onEdit = {
                                    selectedColor = colorItem
                                    nameInput = colorItem.name
                                    hexInput = colorItem.hex.orEmpty()
                                    showFormDialog = true
                                },
                                onDelete = {
                                    selectedColor = colorItem
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
                selectedColor = null
                nameInput = ""
                hexInput = ""
                showFormDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.catalog_colors_add)
            )
        }
    }

    if (showFormDialog) {
        val isEditing = selectedColor != null
        val normalizedHex = normalizeHexInput(hexInput)
        val previewColor = colorFromHexOrNull(normalizedHex)
        val previewTextColor = if ((previewColor ?: MaterialTheme.colorScheme.surface).luminance() > 0.5f) {
            Color.Black
        } else {
            Color.White
        }

        AlertDialog(
            onDismissRequest = { showFormDialog = false },
            title = {
                Text(
                    text = stringResource(
                        if (isEditing) {
                            R.string.catalog_colors_edit_dialog_title
                        } else {
                            R.string.catalog_colors_create_dialog_title
                        }
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text(stringResource(R.string.catalog_colors_name_label)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = hexInput,
                        onValueChange = { hexInput = sanitizeHexInput(it) },
                        label = { Text(stringResource(R.string.catalog_colors_hex_label)) },
                        placeholder = { Text("#FFD700") },
                        supportingText = {
                            Text(text = stringResource(R.string.catalog_colors_hex_supporting_text))
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    ColorPreviewCard(
                        hex = normalizedHex,
                        previewColor = previewColor,
                        previewTextColor = previewTextColor
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = stringResource(R.string.catalog_colors_quick_pick),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            quickColorOptions.forEach { option ->
                                val optionColor = colorFromHexOrNull(option) ?: MaterialTheme.colorScheme.surfaceVariant
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(optionColor)
                                        .border(
                                            width = if (normalizeHexInput(hexInput) == option) 2.dp else 1.dp,
                                            color = if (normalizeHexInput(hexInput) == option) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.outlineVariant
                                            },
                                            shape = CircleShape
                                        )
                                        .clickable { hexInput = option }
                                )
                            }
                        }
                    }

                    TextButton(
                        onClick = { hexInput = "" },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = stringResource(R.string.catalog_colors_clear_hex))
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (nameInput.isBlank()) return@TextButton
                        if (normalizedHex != null && previewColor == null) return@TextButton

                        if (isEditing) {
                            val colorItem = selectedColor ?: return@TextButton
                            awaitingMutationResult = true
                            viewModel.updateColor(
                                id = colorItem.id,
                                name = nameInput,
                                hex = normalizedHex
                            )
                        } else {
                            awaitingMutationResult = true
                            viewModel.createColor(
                                name = nameInput,
                                hex = normalizedHex
                            )
                        }

                        showFormDialog = false
                    }
                ) {
                    Text(
                        text = stringResource(
                            if (isEditing) {
                                R.string.update
                            } else {
                                R.string.create
                            }
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

    if (showDeleteDialog && selectedColor != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = stringResource(R.string.catalog_colors_delete_dialog_title)) },
            text = {
                Text(
                    text = stringResource(
                        R.string.catalog_colors_delete_dialog_message,
                        selectedColor!!.name
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        awaitingMutationResult = true
                        viewModel.deleteColor(id = selectedColor!!.id)
                        showDeleteDialog = false
                        selectedColor = null
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
private fun CatalogColorItem(
    colorItem: CatalogColor,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val swatchColor = colorFromHexOrNull(colorItem.hex) ?: MaterialTheme.colorScheme.surfaceVariant
    val swatchTextColor = if (swatchColor.luminance() > 0.5f) Color.Black else Color.White

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
                            .background(swatchColor)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = colorItem.hex ?: stringResource(R.string.catalog_colors_no_hex_short),
                            color = swatchTextColor,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column {
                        Text(
                            text = stringResource(R.string.catalog_colors_card_label),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = colorItem.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Surface(
                    color = if (colorItem.isActive) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = stringResource(
                            if (colorItem.isActive) {
                                R.string.status_active
                            } else {
                                R.string.status_inactive
                            }
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (colorItem.isActive) {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }

            Text(
                text = stringResource(
                    R.string.catalog_colors_hex_value,
                    colorItem.hex ?: stringResource(R.string.catalog_colors_not_set)
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = stringResource(R.string.catalog_colors_card_id, colorItem.id),
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

@Composable
private fun ColorPreviewCard(
    hex: String?,
    previewColor: Color?,
    previewTextColor: Color
) {
    val displayColor = previewColor ?: MaterialTheme.colorScheme.surfaceVariant

    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(displayColor)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ColorLens,
                    contentDescription = null,
                    tint = previewTextColor
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(R.string.catalog_colors_preview_label),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = when {
                        hex == null -> stringResource(R.string.catalog_colors_preview_none)
                        previewColor == null -> stringResource(R.string.catalog_colors_preview_invalid)
                        else -> hex
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun sanitizeHexInput(input: String): String {
    val uppercase = input.uppercase()
    val filtered = buildString {
        uppercase.forEachIndexed { index, char ->
            if (index == 0 && char == '#') {
                append(char)
            } else if (char in '0'..'9' || char in 'A'..'F') {
                append(char)
            }
        }
    }

    return if (filtered.startsWith("#")) {
        filtered.take(7)
    } else {
        filtered.take(6)
    }
}

private fun normalizeHexInput(input: String): String? {
    val trimmed = input.trim().uppercase()
    if (trimmed.isBlank()) return null

    val withPrefix = if (trimmed.startsWith("#")) trimmed else "#$trimmed"
    return withPrefix.take(7)
}

private fun colorFromHexOrNull(hex: String?): Color? {
    if (hex.isNullOrBlank()) return null
    if (!Regex("^#[0-9A-F]{6}$").matches(hex)) return null

    return runCatching {
        Color(hex.toColorInt())
    }.getOrNull()
}
