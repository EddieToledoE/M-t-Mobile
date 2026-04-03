package com.teddy.mirandaytoledo.register.presentation

import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.Finish
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishColorRelation
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelation
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.Size
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.PriceRule
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductType
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.EducationalLevel
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.SchoolGroup
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.School
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.register.domain.OrderShareInfo
import com.teddy.mirandaytoledo.register.domain.OrderRegistrationResult

enum class RegisterStep {
    PERSONAL,
    PRODUCT,
    PAYMENT
}

data class RegisterUiState(
    val isLoading: Boolean = true,
    val isSubmitting: Boolean = false,
    val error: NetworkError? = null,
    val submitSuccess: OrderRegistrationResult? = null,
    val shareInfo: OrderShareInfo? = null,
    val pendingSaveSuccess: Boolean = false,
    val currentStep: RegisterStep = RegisterStep.PERSONAL,
    val educationalLevels: List<EducationalLevel> = emptyList(),
    val schools: List<School> = emptyList(),
    val schoolGroups: List<SchoolGroup> = emptyList(),
    val productTypes: List<ProductType> = emptyList(),
    val finishes: List<Finish> = emptyList(),
    val sizes: List<Size> = emptyList(),
    val frameModelFinishRelations: List<FrameModelFinishRelation> = emptyList(),
    val frameModelFinishColorRelations: List<FrameModelFinishColorRelation> = emptyList(),
    val priceRules: List<PriceRule> = emptyList(),
    val selectedEducationalLevelId: Int? = null,
    val selectedSchoolId: Int? = null,
    val selectedSchoolGroupId: Int? = null,
    val tutorFirstName: String = "",
    val tutorLastName1: String = "",
    val tutorLastName2: String = "",
    val tutorPhone: String = "",
    val studentFirstName: String = "",
    val studentLastName1: String = "",
    val studentLastName2: String = "",
    val photoReference: String = "",
    val wantsFacialCleanup: Boolean = false,
    val selectedProductTypeId: Int? = null,
    val quantity: String = "1",
    val selectedFinishId: Int? = null,
    val selectedSizeId: Int? = null,
    val selectedFrameModelId: Int? = null,
    val selectedColorId: Int? = null,
    val useManualUnitPrice: Boolean = false,
    val manualUnitPrice: String = "",
    val itemNotes: String = "",
    val orderNotes: String = "",
    val downPayment: String = "",
    val paymentMethod: String = "Cash",
    val paymentNote: String = ""
)
