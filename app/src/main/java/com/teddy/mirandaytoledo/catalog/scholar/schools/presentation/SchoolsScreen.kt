package com.teddy.mirandaytoledo.catalog.scholar.schools.presentation

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.School
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolsScreen(
    modifier: Modifier = Modifier,
    viewModel: SchoolsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Dialog states
    var showFormDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedSchool by remember { mutableStateOf<School?>(null) }

    // Form fields
    var formNameInput by remember { mutableStateOf("") }
    var formSelectedLevelId by remember { mutableStateOf<Int?>(null) }
    var formIsActive by remember { mutableStateOf(true) }

    // Level filter dropdown
    var expandedLevelDropdown by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Escuelas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedSchool = null
                    formNameInput = ""
                    formSelectedLevelId = null
                    formIsActive = true
                    showFormDialog = true
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar escuela")
            }
        }
    ) { paddingValues ->

        when (val state = uiState) {
            is SchoolsUiState.Loading -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SchoolsUiState.Error -> {
                ErrorView(
                    error = state.error,
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    onRetry = { viewModel.retryLoad() }
                )
            }

            is SchoolsUiState.Success -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Search Bar
                    SearchBar(
                        searchQuery = state.searchQuery,
                        onSearchChanged = { viewModel.onSearchChanged(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    // Level Filter Dropdown
                    LevelFilterDropdown(
                        educationalLevels = state.educationalLevels,
                        selectedLevelId = state.selectedLevelId,
                        isExpanded = expandedLevelDropdown,
                        onExpandedChange = { expandedLevelDropdown = it },
                        onLevelSelected = { levelId ->
                            expandedLevelDropdown = false
                            viewModel.onEducationalLevelFilterChanged(levelId)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Schools List
                    if (state.schools.isEmpty() && !state.isLoadingMore) {
                        EmptyView(
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        SchoolsList(
                            schools = state.schools,
                            isLoadingMore = state.isLoadingMore,
                            hasMoreData = state.hasMoreData,
                            onLoadMore = { lastVisibleIndex, totalCount ->
                                viewModel.loadMoreIfNeeded(lastVisibleIndex, totalCount)
                            },
                            onEditClick = { school ->
                                selectedSchool = school
                                formNameInput = school.name
                                formSelectedLevelId = school.educationalLevelId
                                formIsActive = school.isActive
                                showFormDialog = true
                            },
                            onDeleteClick = { school ->
                                selectedSchool = school
                                showDeleteDialog = true
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }

    // Form Dialog (Create/Edit)
    if (showFormDialog) {
        SchoolFormDialog(
            title = if (selectedSchool != null) "Editar Escuela" else "Nueva Escuela",
            nameValue = formNameInput,
            onNameChange = { formNameInput = it },
            selectedLevelId = formSelectedLevelId,
            educationalLevels = (uiState as? SchoolsUiState.Success)?.educationalLevels ?: emptyList(),
            onLevelSelected = { formSelectedLevelId = it },
            isActive = formIsActive,
            onIsActiveChange = { formIsActive = it },
            onDialogDismiss = { showFormDialog = false },
            onConfirm = {
                if (selectedSchool != null) {
                    viewModel.updateSchool(
                        id = selectedSchool!!.id,
                        name = formNameInput,
                        isActive = formIsActive
                    )
                } else {
                    if (formSelectedLevelId != null) {
                        viewModel.createSchool(
                            educationalLevelId = formSelectedLevelId!!,
                            name = formNameInput
                        )
                    }
                }
                showFormDialog = false
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && selectedSchool != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Está seguro que desea desactivar la escuela \"${selectedSchool!!.name}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteSchool(selectedSchool!!.id)
                        showDeleteDialog = false
                        selectedSchool = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChanged,
        modifier = modifier.height(56.dp),
        placeholder = { Text("Buscar escuelas...") },
        singleLine = true,
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchChanged("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Limpiar búsqueda")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LevelFilterDropdown(
    educationalLevels: List<EducationalLevel>,
    selectedLevelId: Int?,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onLevelSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        AssistChip(
            onClick = { onExpandedChange(!isExpanded) },
            label = {
                val selectedLevel =
                    educationalLevels.find { it.id == selectedLevelId }
                Text(
                    text = selectedLevel?.name ?: "Todos los niveles",
                    modifier = Modifier.fillMaxWidth()
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.fillMaxWidth()
        ) {
            DropdownMenuItem(
                text = { Text("Todos los niveles") },
                onClick = {
                    onLevelSelected(null)
                    onExpandedChange(false)
                }
            )

            educationalLevels.forEach { level ->
                DropdownMenuItem(
                    text = { Text(level.name) },
                    onClick = {
                        onLevelSelected(level.id)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@Composable
private fun SchoolsList(
    schools: List<School>,
    isLoadingMore: Boolean,
    hasMoreData: Boolean,
    onLoadMore: (Int, Int) -> Unit,
    onEditClick: (School) -> Unit,
    onDeleteClick: (School) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(schools) { index, school ->
            SchoolCard(
                school = school,
                onEditClick = { onEditClick(school) },
                onDeleteClick = { onDeleteClick(school) }
            )

            // Trigger load more
            if (index == schools.size - 1 && hasMoreData) {
                onLoadMore(index, schools.size)
            }
        }

        // Loading indicator at the end
        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

@Composable
private fun SchoolCard(
    school: School,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header: Name and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = school.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                if (!school.isActive) {
                    AssistChip(
                        onClick = {},
                        label = { Text("Inactiva", fontSize = MaterialTheme.typography.labelSmall.fontSize) }
                    )
                }
            }

            // Level Badge
            AssistChip(
                onClick = {},
                label = { Text(school.educationalLevelName ?: "Sin nivel") }
            )

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun SchoolFormDialog(
    title: String,
    nameValue: String,
    onNameChange: (String) -> Unit,
    selectedLevelId: Int?,
    educationalLevels: List<EducationalLevel>,
    onLevelSelected: (Int?) -> Unit,
    isActive: Boolean,
    onIsActiveChange: (Boolean) -> Unit,
    onDialogDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedLevelDropdown by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDialogDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // School Name Input
                OutlinedTextField(
                    value = nameValue,
                    onValueChange = onNameChange,
                    label = { Text("Nombre de la escuela") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true
                )

                // Educational Level Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    AssistChip(
                        onClick = { expandedLevelDropdown = !expandedLevelDropdown },
                        label = {
                            val selectedLevel =
                                educationalLevels.find { it.id == selectedLevelId }
                            Text(
                                text = selectedLevel?.name ?: "Seleccionar nivel",
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    DropdownMenu(
                        expanded = expandedLevelDropdown,
                        onDismissRequest = { expandedLevelDropdown = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        educationalLevels.forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level.name) },
                                onClick = {
                                    onLevelSelected(level.id)
                                    expandedLevelDropdown = false
                                }
                            )
                        }
                    }
                }

                // Is Active Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Activa")
                    Switch(
                        checked = isActive,
                        onCheckedChange = onIsActiveChange
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = nameValue.isNotBlank() && selectedLevelId != null
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDialogDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun EmptyView(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No hay escuelas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pulsa + para crear una nueva escuela",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorView(
    error: NetworkError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Error al cargar escuelas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = when (error) {
                    NetworkError.NO_INTERNET -> "No hay conexión a internet"
                    NetworkError.REQUEST_TIMEOUT -> "La solicitud tardó demasiado"
                    NetworkError.SERVER_ERROR -> "Error del servidor"
                    NetworkError.INCORRECT_CREDENTIALS -> "Credenciales incorrectas"
                    NetworkError.TOO_MANY_REQUESTS -> "Demasiadas solicitudes"
                    else -> "Error desconocido"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}
