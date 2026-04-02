package com.teddy.mirandaytoledo.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

enum class TimedFeedbackType {
    Success,
    Error
}

data class TimedFeedbackUi(
    val type: TimedFeedbackType,
    val title: String,
    val message: String
)

@Composable
fun TimedFeedbackDialog(
    feedback: TimedFeedbackUi?,
    onDismiss: () -> Unit,
    autoDismissMillis: Long = 3500L
) {
    if (feedback == null) return

    LaunchedEffect(feedback) {
        delay(autoDismissMillis)
        onDismiss()
    }

    val containerColor = when (feedback.type) {
        TimedFeedbackType.Success -> MaterialTheme.colorScheme.secondaryContainer
        TimedFeedbackType.Error -> MaterialTheme.colorScheme.errorContainer
    }
    val contentColor = when (feedback.type) {
        TimedFeedbackType.Success -> MaterialTheme.colorScheme.onSecondaryContainer
        TimedFeedbackType.Error -> MaterialTheme.colorScheme.onErrorContainer
    }
    val icon = when (feedback.type) {
        TimedFeedbackType.Success -> Icons.Default.CheckCircle
        TimedFeedbackType.Error -> Icons.Default.ErrorOutline
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = containerColor,
            tonalElevation = 6.dp,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FeedbackIcon(
                    icon = icon,
                    containerColor = contentColor.copy(alpha = 0.12f),
                    contentColor = contentColor
                )
                Text(
                    text = feedback.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = contentColor
                )
                Text(
                    text = feedback.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor
                )
            }
        }
    }
}

@Composable
private fun FeedbackIcon(
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .background(
                color = containerColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(28.dp)
        )
    }
}
