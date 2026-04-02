package com.teddy.mirandaytoledo.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackDialog
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackType
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackUi
import com.teddy.mirandaytoledo.core.presentation.util.toString
import com.teddy.mirandaytoledo.register.domain.PendingRegistration
import com.teddy.mirandaytoledo.register.domain.PendingRegistrationStatus
import org.koin.androidx.compose.koinViewModel
import java.text.DateFormat
import java.util.Date

@Composable
fun PendingRegistrationsScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var feedback by remember { mutableStateOf<TimedFeedbackUi?>(null) }

    LaunchedEffect(uiState.event) {
        when (val event = uiState.event) {
            HomeEvent.PendingDeleted -> {
                feedback = TimedFeedbackUi(
                    type = TimedFeedbackType.Success,
                    title = context.getString(R.string.feedback_success_title),
                    message = context.getString(R.string.home_pending_deleted_message)
                )
                viewModel.consumeEvent()
            }

            is HomeEvent.PendingSent -> {
                feedback = TimedFeedbackUi(
                    type = TimedFeedbackType.Success,
                    title = context.getString(R.string.home_pending_sent_title, event.orderId),
                    message = context.getString(R.string.home_pending_sent_message)
                )
                viewModel.consumeEvent()
            }

            is HomeEvent.Error -> {
                feedback = TimedFeedbackUi(
                    type = TimedFeedbackType.Error,
                    title = context.getString(R.string.feedback_error_title),
                    message = event.error.toString(context)
                )
                viewModel.consumeEvent()
            }

            else -> Unit
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = stringResource(R.string.home_pending_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.home_pending_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (uiState.pendingRegistrations.isEmpty()) {
            item { EmptyPendingCard() }
        } else {
            items(uiState.pendingRegistrations, key = { it.localId }) { item ->
                PendingRegistrationCard(
                    item = item,
                    isSending = uiState.sendingPendingId == item.localId,
                    onSend = { viewModel.sendPending(item.localId) },
                    onDelete = { viewModel.deletePending(item.localId) }
                )
            }
        }
    }

    TimedFeedbackDialog(
        feedback = feedback,
        onDismiss = { feedback = null }
    )
}

@Composable
private fun EmptyPendingCard() {
    ElevatedCard {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.home_pending_empty_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.home_pending_empty_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PendingRegistrationCard(
    item: PendingRegistration,
    isSending: Boolean,
    onSend: () -> Unit,
    onDelete: () -> Unit
) {
    ElevatedCard {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = item.studentFullName.ifBlank { "-" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.schoolLabel.ifBlank { "-" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusChip(status = item.status)
            }

            PendingInfoLine(
                label = stringResource(R.string.home_pending_product_label),
                value = item.productTypeName
            )
            PendingInfoLine(
                label = stringResource(R.string.home_pending_created_label),
                value = DateFormat.getDateTimeInstance().format(Date(item.createdAt))
            )

            if (!item.lastErrorMessage.isNullOrBlank()) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = item.lastErrorMessage,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDelete) {
                    Text(text = stringResource(R.string.delete))
                }
                TextButton(
                    onClick = onSend,
                    enabled = !isSending && item.status != PendingRegistrationStatus.Syncing
                ) {
                    if (isSending) {
                        CircularProgressIndicator(
                            modifier = Modifier.height(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = stringResource(R.string.home_pending_send_action))
                    }
                }
            }
        }
    }
}

@Composable
private fun PendingInfoLine(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value.ifBlank { "-" },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun StatusChip(status: PendingRegistrationStatus) {
    val (container, content, label) = when (status) {
        PendingRegistrationStatus.PendingSync -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            stringResource(R.string.home_pending_status_pending)
        )
        PendingRegistrationStatus.Syncing -> Triple(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            stringResource(R.string.home_pending_status_syncing)
        )
        PendingRegistrationStatus.Failed -> Triple(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            stringResource(R.string.home_pending_status_failed)
        )
        PendingRegistrationStatus.Synced -> Triple(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            stringResource(R.string.home_pending_status_synced)
        )
    }

    Surface(
        color = container,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            color = content,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
