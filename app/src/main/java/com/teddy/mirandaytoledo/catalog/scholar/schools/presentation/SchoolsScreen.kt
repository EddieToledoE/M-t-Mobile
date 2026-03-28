package com.teddy.mirandaytoledo.catalog.scholar.schools.presentation

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teddy.mirandaytoledo.R
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

    var showFormDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedSchool by remember { mutableStateOf<School?>(null) }

    var formNameInput by remember { mutableStateOf("") }
    var formSelectedLevelId by remember { mutableStateOf<Int?>(null) }
    var formIsActive by remember { mutableStateOf(true) }

    var expandedLevelDropdown by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.schools_title),
                        fontWeight = FontWeight.Bold
                    )
                },
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
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.schools_add)
                )
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
                    FiltersPanel(
                        searchQuery = state.searchQuery,
                        educationalLevels = state.educationalLevels,
                        selectedLevelId = state.selectedLevelId,
                        isExpanded = expandedLevelDropdown,
                        onSearchChanged = { viewModel.onSearchChanged(it) },
                        onExpandedChange = { expandedLevelDropdown = it },
                        onLevelSelected = { levelId ->
                            expandedLevelDropdown = false
                            viewModel.onEducationalLevelFilterChanged(levelId)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )

                    if (state.schools.isEmpty() && !state.isLoadingMore) {
                        EmptyView(modifier = Modifier.fillMaxSize())
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

    if (showFormDialog) {
        SchoolFormDialog(
            title = stringResource(
                if (selectedSchool != null) {
                    R.string.schools_edit_dialog_title
                } else {
                    R.string.schools_create_dialog_title
                }
            ),
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
                } else if (formSelectedLevelId != null) {
                    viewModel.createSchool(
                        educationalLevelId = formSelectedLevelId!!,
                        name = formNameInput
                    )
                }
                showFormDialog = false
            }
        )
    }

    if (showDeleteDialog && selectedSchool != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.schools_delete_dialog_title)) },
            text = {
                Text(
                    stringResource(
                        R.string.schools_delete_dialog_message,
                        selectedSchool!!.name
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteSchool(selectedSchool!!.id)
                        showDeleteDialog = false
                        selectedSchool = null
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
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun FiltersPanel(
    searchQuery: String,
    educationalLevels: List<EducationalLevel>,
    selectedLevelId: Int?,
    isExpanded: Boolean,
    onSearchChanged: (String) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    onLevelSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.schools_filters_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            SearchBar(
                searchQuery = searchQuery,
                onSearchChanged = onSearchChanged,
                modifier = Modifier.fillMaxWidth()
            )

            LevelFilterDropdown(
                educationalLevels = educationalLevels,
                selectedLevelId = selectedLevelId,
                isExpanded = isExpanded,
                onExpandedChange = onExpandedChange,
                onLevelSelected = onLevelSelected,
                modifier = Modifier.fillMaxWidth()
            )
        }
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
        modifier = modifier,
        placeholder = { Text(stringResource(R.string.schools_search_placeholder)) },
        singleLine = true,
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchChanged("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.schools_clear_search)
                    )
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
                val selectedLevel = educationalLevels.find { it.id == selectedLevelId }
                Text(
                    text = selectedLevel?.name ?: stringResource(R.string.schools_all_levels),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.School,
                    contentDescription = null
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
                text = { Text(stringResource(R.string.schools_all_levels)) },
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(schools) { index, school ->
            SchoolCard(
                school = school,
                onEditClick = { onEditClick(school) },
                onDeleteClick = { onDeleteClick(school) }
            )

            if (index == schools.size - 1 && hasMoreData) {
                onLoadMore(index, schools.size)
            }
        }

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
            verticalArrangement = Arrangement.spacedBy(14.dp)
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
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Business,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Column {
                        Text(
                            text = stringResource(R.string.schools_card_label),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = school.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Surface(
                    color = if (school.isActive) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = stringResource(
                            if (school.isActive) {
                                R.string.status_active
                            } else {
                                R.string.status_inactive
                            }
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (school.isActive) {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            school.educationalLevelName
                                ?: stringResource(R.string.schools_without_level)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.School,
                            contentDescription = null
                        )
                    }
                )

                Text(
                    text = stringResource(R.string.schools_card_id, school.id),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(stringResource(R.string.edit))
                }

                TextButton(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(stringResource(R.string.delete))
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
                OutlinedTextField(
                    value = nameValue,
                    onValueChange = onNameChange,
                    label = { Text(stringResource(R.string.schools_name_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    AssistChip(
                        onClick = { expandedLevelDropdown = !expandedLevelDropdown },
                        label = {
                            val selectedLevel = educationalLevels.find { it.id == selectedLevelId }
                            Text(
                                text = selectedLevel?.name
                                    ?: stringResource(R.string.schools_select_level),
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.School,
                                contentDescription = null
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.schools_active_label))
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
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDialogDismiss) {
                Text(stringResource(R.string.cancel))
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
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.schools_empty_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.schools_empty_message),
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
                text = stringResource(R.string.schools_error_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(error.toSchoolErrorStringRes()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}

private fun NetworkError.toSchoolErrorStringRes(): Int {
    return when (this) {
        NetworkError.NO_INTERNET -> R.string.error_no_internet
        NetworkError.REQUEST_TIMEOUT -> R.string.error_request_timeout
        NetworkError.SERVER_ERROR -> R.string.error_server_error
        NetworkError.INCORRECT_CREDENTIALS -> R.string.error_incorrect_credential
        NetworkError.TOO_MANY_REQUESTS -> R.string.error_many_request
        else -> R.string.error_unknown
    }
}
