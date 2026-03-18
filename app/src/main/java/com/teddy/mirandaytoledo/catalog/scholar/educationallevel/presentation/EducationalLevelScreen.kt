package com.teddy.mirandaytoledo.catalog.scholar.educationallevel.presentation

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.EducationalLevel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationalLevelScreen(
    modifier: Modifier = Modifier,
    viewModel: EducationalLevelsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Dialog state
    var showFormDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedLevel by remember { mutableStateOf<EducationalLevel?>(null) }

    // Form fields
    var nameInput by remember { mutableStateOf("") }
    var maxGradeInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Niveles Educativos", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedLevel = null
                    nameInput = ""
                    maxGradeInput = ""
                    showFormDialog = true
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar nivel educativo")
            }
        }
    ) { paddingValues ->

        when (val state = uiState) {
            is EducationalLevelsUiState.Loading -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is EducationalLevelsUiState.Error -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Error al cargar los datos.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { viewModel.loadLevels() }) {
                            Text(text = "Reintentar")
                        }
                    }
                }
            }

            is EducationalLevelsUiState.Success -> {
                if (state.levels.isEmpty()) {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay niveles educativos registrados.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(items = state.levels, key = { it.id }) { level ->
                            EducationalLevelItem(
                                level = level,
                                onEdit = {
                                    selectedLevel = level
                                    nameInput = level.name
                                    maxGradeInput = level.maxGrade.toString()
                                    showFormDialog = true
                                },
                                onDelete = {
                                    selectedLevel = level
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // ---- Form Dialog (Create / Edit) ----
    if (showFormDialog) {
        val isEditing = selectedLevel != null
        AlertDialog(
            onDismissRequest = { showFormDialog = false },
            title = {
                Text(text = if (isEditing) "Editar Nivel Educativo" else "Nuevo Nivel Educativo")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Nombre") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = maxGradeInput,
                        onValueChange = { maxGradeInput = it.filter { c -> c.isDigit() } },
                        label = { Text("Grado máximo") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val maxGrade = maxGradeInput.toIntOrNull() ?: return@TextButton
                        if (nameInput.isBlank()) return@TextButton
                        if (isEditing) {
                            val level = selectedLevel!!
                            viewModel.updateLevel(
                                id = level.id,
                                name = nameInput,
                                maxGrade = maxGrade,
                                isActive = level.isActive
                            )
                        } else {
                            viewModel.createLevel(name = nameInput, maxGrade = maxGrade)
                        }
                        showFormDialog = false
                    }
                ) {
                    Text(text = if (isEditing) "Actualizar" else "Crear")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFormDialog = false }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }

    // ---- Confirm Delete Dialog ----
    if (showDeleteDialog && selectedLevel != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = "Eliminar Nivel Educativo") },
            text = {
                Text(text = "¿Estás seguro de que deseas eliminar \"${selectedLevel!!.name}\"? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteLevel(id = selectedLevel!!.id)
                        showDeleteDialog = false
                        selectedLevel = null
                    }
                ) {
                    Text(text = "Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}

@Composable
private fun EducationalLevelItem(
    level: EducationalLevel,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = level.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Grado máximo: ${level.maxGrade}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
