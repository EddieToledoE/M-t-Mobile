package com.teddy.mirandaytoledo.catalog.scholar.groups.presentation

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
import androidx.compose.material.icons.outlined.Groups
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
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.SchoolGroup
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.School
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolGroupsScreen(
    modifier: Modifier = Modifier,
    viewModel: SchoolGroupsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showFormDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf<SchoolGroup?>(null) }

    var formGroupCodeInput by remember { mutableStateOf("") }
    var formSelectedSchoolId by remember { mutableStateOf<Int?>(null) }
    var formIsActive by remember { mutableStateOf(true) }

    var expandedSchoolDropdown by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.school_groups_title),
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
                    selectedGroup = null
                    formGroupCodeInput = ""
                    formSelectedSchoolId = null
                    formIsActive = true
                    showFormDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.school_groups_add)
                )
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
                    FiltersPanel(
                        searchQuery = state.searchQuery,
                        schools = state.schools,
                        selectedSchoolId = state.selectedSchoolId,
                        isExpanded = expandedSchoolDropdown,
                        onSearchChanged = { viewModel.onSearchChanged(it) },
                        onExpandedChange = { expandedSchoolDropdown = it },
                        onSchoolSelected = { schoolId ->
                            expandedSchoolDropdown = false
                            viewModel.onSchoolFilterChanged(schoolId)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )

                    if (state.schoolGroups.isEmpty() && !state.isLoadingMore) {
                        EmptyView(modifier = Modifier.fillMaxSize())
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

    if (showFormDialog) {
        SchoolGroupFormDialog(
            title = stringResource(
                if (selectedGroup != null) {
                    R.string.school_groups_edit_dialog_title
                } else {
                    R.string.school_groups_create_dialog_title
                }
            ),
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
                } else if (formSelectedSchoolId != null) {
                    viewModel.createSchoolGroup(
                        schoolId = formSelectedSchoolId!!,
                        groupCode = formGroupCodeInput
                    )
                }
                showFormDialog = false
            }
        )
    }

    if (showDeleteDialog && selectedGroup != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.school_groups_delete_dialog_title)) },
            text = {
                Text(
                    stringResource(
                        R.string.school_groups_delete_dialog_message,
                        selectedGroup!!.groupCode
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteSchoolGroup(selectedGroup!!.id)
                        showDeleteDialog = false
                        selectedGroup = null
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
    schools: List<School>,
    selectedSchoolId: Int?,
    isExpanded: Boolean,
    onSearchChanged: (String) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    onSchoolSelected: (Int?) -> Unit,
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
                text = stringResource(R.string.school_groups_filters_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            SearchBar(
                searchQuery = searchQuery,
                onSearchChanged = onSearchChanged,
                modifier = Modifier.fillMaxWidth()
            )

            SchoolFilterDropdown(
                schools = schools,
                selectedSchoolId = selectedSchoolId,
                isExpanded = isExpanded,
                onExpandedChange = onExpandedChange,
                onSchoolSelected = onSchoolSelected,
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
        placeholder = { Text(stringResource(R.string.school_groups_search_placeholder)) },
        singleLine = true,
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchChanged("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.school_groups_clear_search)
                    )
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
                    text = selectedSchool?.name ?: stringResource(R.string.school_groups_all_schools),
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
                text = { Text(stringResource(R.string.school_groups_all_schools)) },
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(groups) { index, group ->
            SchoolGroupCard(
                group = group,
                onEditClick = { onEditClick(group) },
                onDeleteClick = { onDeleteClick(group) }
            )

            if (index == groups.size - 1 && hasMoreData) {
                onLoadMore(index, groups.size)
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
private fun SchoolGroupCard(
    group: SchoolGroup,
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
                            imageVector = Icons.Outlined.Groups,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Column {
                        Text(
                            text = stringResource(R.string.school_groups_card_label),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = stringResource(
                                R.string.school_groups_card_group_code,
                                group.groupCode
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Surface(
                    color = if (group.isActive) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = stringResource(
                            if (group.isActive) {
                                R.string.status_active
                            } else {
                                R.string.status_inactive
                            }
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (group.isActive) {
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
                            group.schoolName
                                ?: stringResource(R.string.school_groups_without_school)
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
                    text = group.educationalLevelName
                        ?: stringResource(
                            R.string.school_groups_level_id_fallback,
                            group.educationalLevelId ?: 0
                        ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = stringResource(R.string.school_groups_card_id, group.id),
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
                OutlinedTextField(
                    value = groupCodeValue,
                    onValueChange = onGroupCodeChange,
                    label = { Text(stringResource(R.string.school_groups_code_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    AssistChip(
                        onClick = { expandedSchoolDropdown = !expandedSchoolDropdown },
                        label = {
                            val selectedSchool = schools.find { it.id == selectedSchoolId }
                            Text(
                                text = selectedSchool?.name
                                    ?: stringResource(R.string.school_groups_select_school),
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.school_groups_active_label))
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
                text = stringResource(R.string.school_groups_empty_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.school_groups_empty_message),
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
                text = stringResource(R.string.school_groups_error_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(error.toSchoolGroupErrorStringRes()),
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

private fun NetworkError.toSchoolGroupErrorStringRes(): Int {
    return when (this) {
        NetworkError.NO_INTERNET -> R.string.error_no_internet
        NetworkError.REQUEST_TIMEOUT -> R.string.error_request_timeout
        NetworkError.SERVER_ERROR -> R.string.error_server_error
        NetworkError.INCORRECT_CREDENTIALS -> R.string.error_incorrect_credential
        NetworkError.TOO_MANY_REQUESTS -> R.string.error_many_request
        else -> R.string.error_unknown
    }
}
