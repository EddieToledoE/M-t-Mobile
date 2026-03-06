package com.teddy.mirandaytoledo.catalog.presentation.schoolgroups

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
import com.teddy.mirandaytoledo.catalog.domain.School
import com.teddy.mirandaytoledo.catalog.domain.SchoolGroup
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolGroupsScreen(
    modifier: Modifier = Modifier,
    viewModel: SchoolGroupsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Dialog states
    var showFormDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf<SchoolGroup?>(null) }

    // Form fields
    var formGroupCodeInput by remember { mutableStateOf("") }
    var formSelectedSchoolId by remember { mutableStateOf<Int?>(null) }
    var formIsActive by remember { mutableStateOf(true) }

    // School filter dropdown
    var expandedSchoolDropdown by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Grupos de Escuelas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedGroup = null
                    formGroupCodeInput = ""
                    formSelectedSchoolId = null
                    formIsActive = true
                    showFormDialog = true
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar grupo")
            }
        }
    ) { paddingValues ->

        when (val state = uiState) {
            is SchoolGroupsUiState.Loading -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SchoolGroupsUiState.Error -> {
                ErrorView(
                    error = state.error,
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    onRetry = { viewModel.retryLoad() }
                )
            }

            is SchoolGroupsUiState.Success -> {
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

                    // School Filter Dropdown
                    SchoolFilterDropdown(
                        schools = state.schools,
                        selectedSchoolId = state.selectedSchoolId,
                        isExpanded = expandedSchoolDropdown,
                        onExpandedChange = { expandedSchoolDropdown = it },
                        onSchoolSelected = { schoolId ->
                            expandedSchoolDropdown = false
                            viewModel.onSchoolFilterChanged(schoolId)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Groups List
                    if (state.schoolGroups.isEmpty() && !state.isLoadingMore) {
                        EmptyView(
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        SchoolGroupsList(
                            groups = state.schoolGroups,
                            isLoadingMore = state.isLoadingMore,
                            hasMoreData = state.hasMoreData,
                            onLoadMore = { lastVisibleIndex, totalCount ->
                                viewModel.loadMoreIfNeeded(lastVisibleIndex, totalCount)
                            },
                            onEditClick = { group ->
                                selectedGroup = group
                                formGroupCodeInput = group.groupCode
                                formSelectedSchoolId = group.schoolId
                                formIsActive = group.isActive
                                showFormDialog = true
                            },
                            onDeleteClick = { group ->
                                selectedGroup = group
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
        SchoolGroupFormDialog(
            title = if (selectedGroup != null) "Editar Grupo" else "Nuevo Grupo",
            groupCodeValue = formGroupCodeInput,
            onGroupCodeChange = { formGroupCodeInput = it },
            selectedSchoolId = formSelectedSchoolId,
            schools = (uiState as? SchoolGroupsUiState.Success)?.schools ?: emptyList(),
            onSchoolSelected = { formSelectedSchoolId = it },
            isActive = formIsActive,
            onIsActiveChange = { formIsActive = it },
            onDialogDismiss = { showFormDialog = false },
            onConfirm = {
                if (selectedGroup != null) {
                    viewModel.updateSchoolGroup(
                        id = selectedGroup!!.id,
                        groupCode = formGroupCodeInput,
                        isActive = formIsActive
                    )
                } else {
                    if (formSelectedSchoolId != null) {
                        viewModel.createSchoolGroup(
                            schoolId = formSelectedSchoolId!!,
                            groupCode = formGroupCodeInput
                        )
                    }
                }
                showFormDialog = false
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && selectedGroup != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Está seguro que desea desactivar el grupo \"${selectedGroup!!.groupCode}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteSchoolGroup(selectedGroup!!.id)
                        showDeleteDialog = false
                        selectedGroup = null
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
        placeholder = { Text("Buscar por código...") },
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
private fun SchoolFilterDropdown(
    schools: List<School>,
    selectedSchoolId: Int?,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSchoolSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        AssistChip(
            onClick = { onExpandedChange(!isExpanded) },
            label = {
                val selectedSchool = schools.find { it.id == selectedSchoolId }
                Text(
                    text = selectedSchool?.name ?: "Todas las escuelas",
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
                text = { Text("Todas las escuelas") },
                onClick = {
                    onSchoolSelected(null)
                    onExpandedChange(false)
                }
            )

            schools.forEach { school ->
                DropdownMenuItem(
                    text = { Text(school.name) },
                    onClick = {
                        onSchoolSelected(school.id)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@Composable
private fun SchoolGroupsList(
    groups: List<SchoolGroup>,
    isLoadingMore: Boolean,
    hasMoreData: Boolean,
    onLoadMore: (Int, Int) -> Unit,
    onEditClick: (SchoolGroup) -> Unit,
    onDeleteClick: (SchoolGroup) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(groups) { index, group ->
            SchoolGroupCard(
                group = group,
                onEditClick = { onEditClick(group) },
                onDeleteClick = { onDeleteClick(group) }
            )

            // Trigger load more
            if (index == groups.size - 1 && hasMoreData) {
                onLoadMore(index, groups.size)
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
private fun SchoolGroupCard(
    group: SchoolGroup,
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
            // Header: GroupCode and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Grupo: ${group.groupCode}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                if (!group.isActive) {
                    AssistChip(
                        onClick = {},
                        label = { Text("Inactivo", fontSize = MaterialTheme.typography.labelSmall.fontSize) }
                    )
                }
            }

            // School ID
            Text(
                text = "Escuela ID: ${group.schoolId}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
private fun SchoolGroupFormDialog(
    title: String,
    groupCodeValue: String,
    onGroupCodeChange: (String) -> Unit,
    selectedSchoolId: Int?,
    schools: List<School>,
    onSchoolSelected: (Int?) -> Unit,
    isActive: Boolean,
    onIsActiveChange: (Boolean) -> Unit,
    onDialogDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedSchoolDropdown by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDialogDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Group Code Input
                OutlinedTextField(
                    value = groupCodeValue,
                    onValueChange = onGroupCodeChange,
                    label = { Text("Código de grupo") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true
                )

                // School Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    AssistChip(
                        onClick = { expandedSchoolDropdown = !expandedSchoolDropdown },
                        label = {
                            val selectedSchool = schools.find { it.id == selectedSchoolId }
                            Text(
                                text = selectedSchool?.name ?: "Seleccionar escuela",
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    DropdownMenu(
                        expanded = expandedSchoolDropdown,
                        onDismissRequest = { expandedSchoolDropdown = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        schools.forEach { school ->
                            DropdownMenuItem(
                                text = { Text(school.name) },
                                onClick = {
                                    onSchoolSelected(school.id)
                                    expandedSchoolDropdown = false
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
                    Text("Activo")
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
                enabled = groupCodeValue.isNotBlank() && selectedSchoolId != null
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
                text = "No hay grupos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pulsa + para crear un nuevo grupo",
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
                text = "Error al cargar grupos",
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
