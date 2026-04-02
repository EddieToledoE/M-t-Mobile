package com.teddy.mirandaytoledo.home.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teddy.mirandaytoledo.R

@Composable
fun RowScope.HomeTopBarActions(
    hasPendingRecords: Boolean,
    isSyncing: Boolean,
    onSyncClick: () -> Unit,
    onPendingClick: () -> Unit
) {
    IconButton(
        onClick = onSyncClick,
        enabled = !isSyncing
    ) {
        Icon(
            imageVector = Icons.Default.CloudSync,
            contentDescription = stringResource(R.string.home_sync_action)
        )
    }

    IconButton(
        onClick = onPendingClick,
        enabled = hasPendingRecords
    ) {
        Icon(
            imageVector = Icons.Default.Inventory2,
            contentDescription = stringResource(R.string.home_pending_title)
        )
    }
}
