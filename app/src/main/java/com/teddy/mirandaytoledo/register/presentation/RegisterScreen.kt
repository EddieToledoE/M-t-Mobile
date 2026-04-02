@file:OptIn(ExperimentalMaterial3Api::class)

package com.teddy.mirandaytoledo.register.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductType
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackDialog
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackType
import com.teddy.mirandaytoledo.core.presentation.components.TimedFeedbackUi
import com.teddy.mirandaytoledo.core.presentation.util.toString
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

private val paymentMethods = listOf("Cash", "Transfer")

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var feedback by remember { mutableStateOf<TimedFeedbackUi?>(null) }
    var awaitingSubmitResult by remember { mutableStateOf(false) }

    val availableSchools = uiState.schools.filter {
        uiState.selectedEducationalLevelId == null || it.educationalLevelId == uiState.selectedEducationalLevelId
    }
    val availableGroups = uiState.schoolGroups.filter {
        uiState.selectedSchoolId == null || it.schoolId == uiState.selectedSchoolId
    }
    val selectedProductType = uiState.productTypes.firstOrNull { it.id == uiState.selectedProductTypeId }
    val availableSizes = uiState.sizes.filter { size ->
        selectedProductType?.requiresSize == true &&
            selectedProductType.allowedSizeGroup.equals(size.sizeGroup, ignoreCase = true)
    }
    val availableModels = uiState.frameModelFinishRelations.filter {
        uiState.selectedFinishId != null && it.finishId == uiState.selectedFinishId
    }
    val availableColors = uiState.frameModelFinishColorRelations.filter {
        uiState.selectedFinishId != null &&
            uiState.selectedFrameModelId != null &&
            it.finishId == uiState.selectedFinishId &&
            it.frameModelId == uiState.selectedFrameModelId
    }
    val productSelectionReadyForPricing = selectedProductType != null &&
        (!selectedProductType.requiresFinish || uiState.selectedFinishId != null) &&
        (!selectedProductType.requiresSize || uiState.selectedSizeId != null)

    val quantity = uiState.quantity.toIntOrNull()
    val manualUnitPrice = uiState.manualUnitPrice.toDoubleOrNull()
    val estimatedUnitPrice = when {
        selectedProductType == null -> null
        uiState.useManualUnitPrice && manualUnitPrice != null && manualUnitPrice > 0.0 -> manualUnitPrice
        !selectedProductType.requiresFinish && !selectedProductType.requiresSize -> manualUnitPrice
        else -> uiState.priceRules.firstOrNull {
            it.productTypeId == uiState.selectedProductTypeId &&
                it.finishId == uiState.selectedFinishId &&
                it.sizeId == uiState.selectedSizeId
        }?.price
    }
    val estimatedTotal = if (estimatedUnitPrice != null && quantity != null) {
        estimatedUnitPrice * quantity
    } else null
    val downPayment = uiState.downPayment.toDoubleOrNull()
    val remainingBalance = if (estimatedTotal != null && downPayment != null) {
        (estimatedTotal - downPayment).coerceAtLeast(0.0)
    } else estimatedTotal

    val canContinuePersonal = uiState.tutorFirstName.isNotBlank() &&
        uiState.tutorLastName1.isNotBlank() &&
        uiState.studentFirstName.isNotBlank() &&
        uiState.studentLastName1.isNotBlank() &&
        uiState.selectedSchoolGroupId != null

    val canContinueProduct = selectedProductType != null &&
        quantity != null &&
        quantity > 0 &&
        (!selectedProductType.requiresFinish || uiState.selectedFinishId != null) &&
        (!selectedProductType.requiresSize || uiState.selectedSizeId != null) &&
        (!selectedProductType.requiresFrameModel || uiState.selectedFrameModelId != null) &&
        (!selectedProductType.requiresColor || uiState.selectedColorId != null) &&
        (
            if (uiState.useManualUnitPrice) {
                manualUnitPrice != null && manualUnitPrice > 0.0
            } else {
                estimatedUnitPrice != null && estimatedUnitPrice > 0.0
            }
        )

    val canSubmit = canContinuePersonal &&
        canContinueProduct &&
        (uiState.downPayment.isBlank() || (downPayment != null && downPayment > 0.0))

    LaunchedEffect(
        uiState.isSubmitting,
        uiState.submitSuccess,
        uiState.pendingSaveSuccess,
        uiState.error,
        awaitingSubmitResult
    ) {
        if (uiState.pendingSaveSuccess) {
            feedback = TimedFeedbackUi(
                type = TimedFeedbackType.Success,
                title = context.getString(R.string.register_pending_saved_title),
                message = context.getString(R.string.register_pending_saved_message)
            )
            awaitingSubmitResult = false
            return@LaunchedEffect
        }

        if (!awaitingSubmitResult || uiState.isSubmitting) return@LaunchedEffect

        when {
            uiState.submitSuccess != null -> {
                val result = uiState.submitSuccess ?: return@LaunchedEffect
                feedback = TimedFeedbackUi(
                    type = TimedFeedbackType.Success,
                    title = context.getString(R.string.register_success_title, result.orderId),
                    message = context.getString(
                        R.string.register_success_summary,
                        result.total,
                        result.paid,
                        result.remaining
                    )
                )
                awaitingSubmitResult = false
            }

            uiState.error != null -> {
                feedback = TimedFeedbackUi(
                    type = TimedFeedbackType.Error,
                    title = context.getString(R.string.feedback_error_title),
                    message = uiState.error!!.toString(context)
                )
                awaitingSubmitResult = false
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Stepper(currentStep = uiState.currentStep)
                RegisterHeader(currentStep = uiState.currentStep)

                if (uiState.productTypes.isEmpty()) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(
                            text = stringResource(R.string.register_empty_catalogs_message),
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        when (uiState.currentStep) {
                            RegisterStep.PERSONAL -> RegisterPersonalStep(
                                uiState = uiState,
                                availableSchools = availableSchools,
                                availableGroups = availableGroups,
                                onStateChange = viewModel::updateState
                            )

                            RegisterStep.PRODUCT -> RegisterProductStep(
                                uiState = uiState,
                                selectedProductType = selectedProductType,
                                availableSizes = availableSizes,
                                availableModels = availableModels,
                                availableColors = availableColors,
                                productSelectionReadyForPricing = productSelectionReadyForPricing,
                                estimatedUnitPrice = estimatedUnitPrice,
                                estimatedTotal = estimatedTotal,
                                onStateChange = viewModel::updateState
                            )

                            RegisterStep.PAYMENT -> RegisterPaymentStep(
                                uiState = uiState,
                                selectedProductType = selectedProductType,
                                estimatedTotal = estimatedTotal,
                                paid = downPayment,
                                remaining = remainingBalance,
                                onStateChange = viewModel::updateState
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = { viewModel.previousStep() },
                        enabled = uiState.currentStep != RegisterStep.PERSONAL,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(R.string.register_back_button))
                    }

                    Button(
                        onClick = {
                            when (uiState.currentStep) {
                                RegisterStep.PERSONAL -> viewModel.nextStep()
                                RegisterStep.PRODUCT -> viewModel.nextStep()
                                RegisterStep.PAYMENT -> {
                                    awaitingSubmitResult = true
                                    viewModel.submitRegistration()
                                }
                            }
                        },
                        enabled = when (uiState.currentStep) {
                            RegisterStep.PERSONAL -> canContinuePersonal
                            RegisterStep.PRODUCT -> canContinueProduct
                            RegisterStep.PAYMENT -> canSubmit && !uiState.isSubmitting
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        if (uiState.isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.width(18.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = stringResource(
                                    if (uiState.currentStep == RegisterStep.PAYMENT) {
                                        R.string.register_submit_button
                                    } else {
                                        R.string.register_next_button
                                    }
                                )
                            )
                        }
                    }
                }

                if (uiState.currentStep == RegisterStep.PAYMENT) {
                    TextButton(
                        onClick = { viewModel.savePendingRegistration() },
                        enabled = canSubmit && !uiState.isSubmitting,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.register_save_local_button))
                    }
                }
            }
        }

        TimedFeedbackDialog(
            feedback = feedback,
            onDismiss = { feedback = null }
        )
    }
}

@Composable
private fun RegisterHeader(currentStep: RegisterStep) {
    val (title, subtitle, icon) = when (currentStep) {
        RegisterStep.PERSONAL -> Triple(
            stringResource(R.string.register_step_personal_title),
            stringResource(R.string.register_step_personal_subtitle),
            Icons.Default.Person
        )
        RegisterStep.PRODUCT -> Triple(
            stringResource(R.string.register_step_product_title),
            stringResource(R.string.register_step_product_subtitle),
            Icons.Default.LocalOffer
        )
        RegisterStep.PAYMENT -> Triple(
            stringResource(R.string.register_step_payment_title),
            stringResource(R.string.register_step_payment_subtitle),
            Icons.Default.Payments
        )
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(12.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun Stepper(currentStep: RegisterStep) {
    val steps = listOf(
        RegisterStep.PERSONAL to stringResource(R.string.register_step_short_personal),
        RegisterStep.PRODUCT to stringResource(R.string.register_step_short_product),
        RegisterStep.PAYMENT to stringResource(R.string.register_step_short_payment)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, (step, label) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = when {
                                step == currentStep -> MaterialTheme.colorScheme.primary
                                step.ordinal < currentStep.ordinal -> MaterialTheme.colorScheme.secondary
                                else -> MaterialTheme.colorScheme.outlineVariant
                            },
                            shape = CircleShape
                        )
                        .background(
                            color = when {
                                step == currentStep -> MaterialTheme.colorScheme.primary
                                step.ordinal < currentStep.ordinal -> MaterialTheme.colorScheme.secondary
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (index + 1).toString(),
                        color = when {
                            step == currentStep -> MaterialTheme.colorScheme.onPrimary
                            step.ordinal < currentStep.ordinal -> MaterialTheme.colorScheme.onSecondary
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (step == currentStep) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

@Composable
private fun RegisterPersonalStep(
    uiState: RegisterUiState,
    availableSchools: List<com.teddy.mirandaytoledo.catalog.scholar.schools.domain.School>,
    availableGroups: List<com.teddy.mirandaytoledo.catalog.scholar.groups.domain.SchoolGroup>,
    onStateChange: ((RegisterUiState) -> RegisterUiState) -> Unit
) {
    SelectorField(
        label = stringResource(R.string.register_educational_level_label),
        items = uiState.educationalLevels,
        selectedLabel = uiState.educationalLevels.firstOrNull {
            it.id == uiState.selectedEducationalLevelId
        }?.name,
        itemLabel = { it.name },
        onSelected = {
            onStateChange { state ->
                state.copy(
                    selectedEducationalLevelId = it.id,
                    selectedSchoolId = null,
                    selectedSchoolGroupId = null
                )
            }
        }
    )
    if (uiState.selectedEducationalLevelId != null && availableSchools.isEmpty()) {
        ValidationMessage(text = stringResource(R.string.register_validation_no_schools))
    }
    SelectorField(
        label = stringResource(R.string.register_school_label),
        items = availableSchools,
        selectedLabel = availableSchools.firstOrNull { it.id == uiState.selectedSchoolId }?.name,
        itemLabel = { it.name },
        enabled = availableSchools.isNotEmpty(),
        onSelected = {
            onStateChange { state ->
                state.copy(
                    selectedSchoolId = it.id,
                    selectedSchoolGroupId = null
                )
            }
        }
    )
    if (uiState.selectedSchoolId != null && availableGroups.isEmpty()) {
        ValidationMessage(text = stringResource(R.string.register_validation_no_groups))
    }
    SelectorField(
        label = stringResource(R.string.register_group_label_new),
        items = availableGroups,
        selectedLabel = availableGroups.firstOrNull { it.id == uiState.selectedSchoolGroupId }?.groupCode,
        itemLabel = { "${it.groupCode} • ${it.schoolName ?: ""}" },
        enabled = availableGroups.isNotEmpty(),
        onSelected = {
            onStateChange { state -> state.copy(selectedSchoolGroupId = it.id) }
        }
    )
    HorizontalDivider()
    TextInput(
        label = stringResource(R.string.register_student_first_name),
        value = uiState.studentFirstName,
        onValueChange = { onStateChange { state -> state.copy(studentFirstName = it) } },
        isError = uiState.studentFirstName.isBlank(),
        supportingText = if (uiState.studentFirstName.isBlank()) {
            stringResource(R.string.register_validation_required)
        } else {
            null
        }
    )
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        TextInput(
            label = stringResource(R.string.register_student_last_name1),
            value = uiState.studentLastName1,
            onValueChange = { onStateChange { state -> state.copy(studentLastName1 = it) } },
            modifier = Modifier.weight(1f),
            isError = uiState.studentLastName1.isBlank(),
            supportingText = if (uiState.studentLastName1.isBlank()) {
                stringResource(R.string.register_validation_required)
            } else {
                null
            }
        )
        TextInput(
            label = stringResource(R.string.register_student_last_name2),
            value = uiState.studentLastName2,
            onValueChange = { onStateChange { state -> state.copy(studentLastName2 = it) } },
            modifier = Modifier.weight(1f)
        )
    }
    TextInput(
        label = stringResource(R.string.register_photo_reference_label),
        value = uiState.photoReference,
        onValueChange = { onStateChange { state -> state.copy(photoReference = it) } },
        supportingText = stringResource(R.string.register_photo_reference_supporting)
    )
    SwitchRow(
        title = stringResource(R.string.register_facial_cleanup_label),
        subtitle = stringResource(R.string.register_facial_cleanup_supporting),
        checked = uiState.wantsFacialCleanup,
        onCheckedChange = { onStateChange { state -> state.copy(wantsFacialCleanup = it) } }
    )
    HorizontalDivider()
    TextInput(
        label = stringResource(R.string.register_tutor_first_name),
        value = uiState.tutorFirstName,
        onValueChange = { onStateChange { state -> state.copy(tutorFirstName = it) } },
        isError = uiState.tutorFirstName.isBlank(),
        supportingText = if (uiState.tutorFirstName.isBlank()) {
            stringResource(R.string.register_validation_required)
        } else {
            null
        }
    )
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        TextInput(
            label = stringResource(R.string.register_tutor_last_name1),
            value = uiState.tutorLastName1,
            onValueChange = { onStateChange { state -> state.copy(tutorLastName1 = it) } },
            modifier = Modifier.weight(1f),
            isError = uiState.tutorLastName1.isBlank(),
            supportingText = if (uiState.tutorLastName1.isBlank()) {
                stringResource(R.string.register_validation_required)
            } else {
                null
            }
        )
        TextInput(
            label = stringResource(R.string.register_tutor_last_name2),
            value = uiState.tutorLastName2,
            onValueChange = { onStateChange { state -> state.copy(tutorLastName2 = it) } },
            modifier = Modifier.weight(1f)
        )
    }
    TextInput(
        label = stringResource(R.string.register_tutor_phone_label),
        value = uiState.tutorPhone,
        onValueChange = {
            val digitsOnly = it.filter(Char::isDigit).take(10)
            onStateChange { state -> state.copy(tutorPhone = digitsOnly) }
        },
        keyboardType = KeyboardType.Phone,
        isError = uiState.tutorPhone.isNotBlank() && uiState.tutorPhone.length != 10,
        supportingText = if (uiState.tutorPhone.isNotBlank() && uiState.tutorPhone.length != 10) {
            stringResource(R.string.register_phone_exact_digits)
        } else {
            stringResource(R.string.register_phone_supporting)
        }
    )
}

@Composable
private fun RegisterProductStep(
    uiState: RegisterUiState,
    selectedProductType: ProductType?,
    availableSizes: List<com.teddy.mirandaytoledo.catalog.frames.sizes.domain.Size>,
    availableModels: List<com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelation>,
    availableColors: List<com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishColorRelation>,
    productSelectionReadyForPricing: Boolean,
    estimatedUnitPrice: Double?,
    estimatedTotal: Double?,
    onStateChange: ((RegisterUiState) -> RegisterUiState) -> Unit
) {
    SelectorField(
        label = stringResource(R.string.register_product_type_label),
        items = uiState.productTypes,
        selectedLabel = selectedProductType?.name,
        itemLabel = { it.name },
        onSelected = {
            onStateChange { state ->
                state.copy(
                    selectedProductTypeId = it.id,
                    selectedFinishId = null,
                    selectedSizeId = null,
                    selectedFrameModelId = null,
                    selectedColorId = null,
                    useManualUnitPrice = false,
                    manualUnitPrice = ""
                )
            }
        }
    )
    TextInput(
        label = stringResource(R.string.register_quantity_label),
        value = uiState.quantity,
        onValueChange = { onStateChange { state -> state.copy(quantity = it.filter(Char::isDigit)) } },
        keyboardType = KeyboardType.Number,
        isError = uiState.quantity.isBlank(),
        supportingText = if (uiState.quantity.isBlank()) {
            stringResource(R.string.register_validation_required)
        } else {
            null
        }
    )
    if (selectedProductType?.requiresFinish == true) {
        SelectorField(
            label = stringResource(R.string.register_finish_label),
            items = uiState.finishes,
            selectedLabel = uiState.finishes.firstOrNull { it.id == uiState.selectedFinishId }?.name,
            itemLabel = { it.name },
            onSelected = {
                onStateChange { state ->
                    state.copy(
                        selectedFinishId = it.id,
                        selectedFrameModelId = null,
                        selectedColorId = null
                    )
                }
            }
        )
        if (uiState.selectedFinishId != null && availableModels.isEmpty() && selectedProductType.requiresFrameModel) {
            ValidationMessage(text = stringResource(R.string.register_validation_no_models))
        }
    }
    if (selectedProductType?.requiresSize == true) {
        SelectorField(
            label = stringResource(R.string.register_size_label_new),
            items = availableSizes,
            selectedLabel = availableSizes.firstOrNull { it.id == uiState.selectedSizeId }?.name,
            itemLabel = { "${it.name} (${it.sizeGroup})" },
            enabled = availableSizes.isNotEmpty(),
            onSelected = { onStateChange { state -> state.copy(selectedSizeId = it.id) } }
        )
        if (selectedProductType.allowedSizeGroup != null && availableSizes.isEmpty()) {
            ValidationMessage(
                text = stringResource(
                    R.string.register_validation_no_sizes,
                    selectedProductType.allowedSizeGroup
                )
            )
        }
    }
    if (selectedProductType?.requiresFrameModel == true) {
        SelectorField(
            label = stringResource(R.string.register_frame_model_label),
            items = availableModels,
            selectedLabel = availableModels.firstOrNull {
                it.frameModelId == uiState.selectedFrameModelId
            }?.frameModelName,
            itemLabel = { it.frameModelName },
            enabled = availableModels.isNotEmpty(),
            onSelected = {
                onStateChange { state ->
                    state.copy(
                        selectedFrameModelId = it.frameModelId,
                        selectedColorId = null
                    )
                }
            }
        )
        if (uiState.selectedFrameModelId != null && availableColors.isEmpty() && selectedProductType.requiresColor) {
            ValidationMessage(text = stringResource(R.string.register_validation_no_colors))
        }
    }
    if (selectedProductType?.requiresColor == true) {
        SelectorField(
            label = stringResource(R.string.register_color_label_new),
            items = availableColors,
            selectedLabel = availableColors.firstOrNull { it.colorId == uiState.selectedColorId }?.colorName,
            itemLabel = { it.colorName },
            enabled = availableColors.isNotEmpty(),
            onSelected = { onStateChange { state -> state.copy(selectedColorId = it.colorId) } }
        )
    }
    if (selectedProductType != null) {
        SwitchRow(
            title = stringResource(R.string.register_manual_price_switch_label),
            subtitle = stringResource(R.string.register_manual_price_switch_supporting),
            checked = uiState.useManualUnitPrice,
            onCheckedChange = {
                onStateChange { state ->
                    state.copy(
                        useManualUnitPrice = it,
                        manualUnitPrice = if (it) state.manualUnitPrice else ""
                    )
                }
            }
        )
    }
    if (selectedProductType != null && uiState.useManualUnitPrice) {
        TextInput(
            label = stringResource(R.string.register_manual_price_label),
            value = uiState.manualUnitPrice,
            onValueChange = {
                onStateChange { state ->
                    state.copy(manualUnitPrice = sanitizeDecimalInput(it))
                }
            },
            keyboardType = KeyboardType.Decimal,
            isError = uiState.manualUnitPrice.isBlank(),
            supportingText = if (uiState.manualUnitPrice.isBlank()) {
                stringResource(R.string.register_validation_required)
            } else {
                null
            }
        )
    }
    TextInput(
        label = stringResource(R.string.register_item_notes_label),
        value = uiState.itemNotes,
        onValueChange = { onStateChange { state -> state.copy(itemNotes = it) } },
        singleLine = false,
        supportingText = stringResource(R.string.register_item_notes_supporting)
    )
    if (!uiState.useManualUnitPrice && productSelectionReadyForPricing && estimatedUnitPrice == null) {
        ValidationMessage(
            text = stringResource(R.string.register_validation_missing_price_rule),
            isError = true
        )
    }
    PricingSummary(unitPrice = estimatedUnitPrice, total = estimatedTotal)
}

@Composable
private fun RegisterPaymentStep(
    uiState: RegisterUiState,
    selectedProductType: ProductType?,
    estimatedTotal: Double?,
    paid: Double?,
    remaining: Double?,
    onStateChange: ((RegisterUiState) -> RegisterUiState) -> Unit
) {
    TextInput(
        label = stringResource(R.string.register_order_notes_label),
        value = uiState.orderNotes,
        onValueChange = { onStateChange { state -> state.copy(orderNotes = it) } },
        singleLine = false
    )
    TextInput(
        label = stringResource(R.string.register_down_payment_label),
        value = uiState.downPayment,
        onValueChange = { onStateChange { state -> state.copy(downPayment = it) } },
        keyboardType = KeyboardType.Decimal,
        supportingText = stringResource(R.string.register_down_payment_supporting)
    )
    if (uiState.downPayment.isNotBlank()) {
        SelectorField(
            label = stringResource(R.string.register_payment_method_label),
            items = paymentMethods,
            selectedLabel = uiState.paymentMethod,
            itemLabel = { it },
            onSelected = { onStateChange { state -> state.copy(paymentMethod = it) } }
        )
        TextInput(
            label = stringResource(R.string.register_payment_note_label),
            value = uiState.paymentNote,
            onValueChange = { onStateChange { state -> state.copy(paymentNote = it) } },
            singleLine = false
        )
    }
    ReceiptSummaryCard(
        uiState = uiState,
        selectedProductType = selectedProductType,
        estimatedTotal = estimatedTotal,
        paid = paid,
        remaining = remaining
    )
    PaymentSummary(total = estimatedTotal, paid = paid, remaining = remaining)
}

@Composable
private fun TextInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = if (singleLine) ImeAction.Next else ImeAction.Default,
    isError: Boolean = false,
    supportingText: String? = null
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine,
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onDone = { focusManager.clearFocus() }
        ),
        supportingText = supportingText?.let { support ->
            {
                Text(text = support)
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun <T> SelectorField(
    label: String,
    items: List<T>,
    selectedLabel: String?,
    itemLabel: (T) -> String,
    enabled: Boolean = true,
    onSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedLabel.orEmpty(),
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(itemLabel(item)) },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.SemiBold)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun PricingSummary(unitPrice: Double?, total: Double?) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.register_pricing_summary_title),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = unitPrice?.let {
                    stringResource(R.string.register_pricing_unit_price, it)
                } ?: stringResource(R.string.register_pricing_unit_price_pending),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = total?.let {
                    stringResource(R.string.register_pricing_total, it)
                } ?: stringResource(R.string.register_pricing_total_pending),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PaymentSummary(total: Double?, paid: Double?, remaining: Double?) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.register_payment_summary_title),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = total?.let {
                    stringResource(R.string.register_pricing_total, it)
                } ?: stringResource(R.string.register_pricing_total_pending),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.register_payment_paid, paid ?: 0.0),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = remaining?.let {
                    stringResource(R.string.register_payment_remaining, it)
                } ?: stringResource(R.string.register_payment_remaining_pending),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ReceiptSummaryCard(
    uiState: RegisterUiState,
    selectedProductType: ProductType?,
    estimatedTotal: Double?,
    paid: Double?,
    remaining: Double?
) {
    val selectedLevel = uiState.educationalLevels.firstOrNull { it.id == uiState.selectedEducationalLevelId }
    val selectedSchool = uiState.schools.firstOrNull { it.id == uiState.selectedSchoolId }
    val selectedGroup = uiState.schoolGroups.firstOrNull { it.id == uiState.selectedSchoolGroupId }
    val selectedFinish = uiState.finishes.firstOrNull { it.id == uiState.selectedFinishId }
    val selectedSize = uiState.sizes.firstOrNull { it.id == uiState.selectedSizeId }
    val selectedModel = uiState.frameModelFinishRelations.firstOrNull { it.frameModelId == uiState.selectedFrameModelId }
    val selectedColor = uiState.frameModelFinishColorRelations.firstOrNull { it.colorId == uiState.selectedColorId }

    Surface(
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.register_receipt_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.miranda_y_toledo),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Image(
                    painter = painterResource(R.drawable.logomytcolor),
                    contentDescription = null,
                    modifier = Modifier
                        .height(52.dp)
                        .width(52.dp),
                    contentScale = ContentScale.Fit
                )
            }
            ReceiptLine(
                label = stringResource(R.string.register_receipt_student),
                value = listOf(
                    uiState.studentFirstName,
                    uiState.studentLastName1,
                    uiState.studentLastName2
                ).filter { it.isNotBlank() }.joinToString(" ")
            )
            ReceiptLine(
                label = stringResource(R.string.register_receipt_tutor),
                value = listOf(
                    uiState.tutorFirstName,
                    uiState.tutorLastName1,
                    uiState.tutorLastName2
                ).filter { it.isNotBlank() }.joinToString(" ")
            )
            ReceiptLine(
                label = stringResource(R.string.register_receipt_school),
                value = listOfNotNull(selectedLevel?.name, selectedSchool?.name, selectedGroup?.groupCode)
                    .joinToString(" • ")
            )
            HorizontalDivider()
            ReceiptLine(
                label = stringResource(R.string.register_receipt_product_type),
                value = selectedProductType?.name.orEmpty()
            )
            ReceiptLine(
                label = stringResource(R.string.register_receipt_options),
                value = listOfNotNull(
                    selectedFinish?.name,
                    selectedSize?.name,
                    selectedModel?.frameModelName,
                    selectedColor?.colorName
                ).ifEmpty { listOf(stringResource(R.string.register_receipt_no_extra_options)) }
                    .joinToString(" • ")
            )
            ReceiptLine(
                label = stringResource(R.string.register_receipt_quantity),
                value = uiState.quantity.ifBlank { "-" }
            )
            HorizontalDivider()
            ReceiptLine(
                label = stringResource(R.string.register_receipt_total),
                value = estimatedTotal?.let { String.format(Locale.US, "$%.2f", it) } ?: "-"
            )
            ReceiptLine(
                label = stringResource(R.string.register_receipt_paid),
                value = paid?.let { String.format(Locale.US, "$%.2f", it) } ?: "$0.00"
            )
            ReceiptLine(
                label = stringResource(R.string.register_receipt_remaining),
                value = remaining?.let { String.format(Locale.US, "$%.2f", it) } ?: "-"
            )
        }
    }
}

@Composable
private fun ReceiptLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.42f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = value.ifBlank { "-" },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(0.58f)
        )
    }
}

@Composable
private fun ValidationMessage(
    text: String,
    isError: Boolean = false
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
    )
}

private fun sanitizeDecimalInput(value: String): String {
    val filtered = value.filter { it.isDigit() || it == '.' }
    val firstDotIndex = filtered.indexOf('.')
    if (firstDotIndex == -1) return filtered

    val integerPart = filtered.substring(0, firstDotIndex + 1)
    val decimalPart = filtered.substring(firstDotIndex + 1).replace(".", "")
    return integerPart + decimalPart
}
