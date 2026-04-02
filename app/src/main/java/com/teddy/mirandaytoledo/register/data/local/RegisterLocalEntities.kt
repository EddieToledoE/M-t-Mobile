package com.teddy.mirandaytoledo.register.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teddy.mirandaytoledo.register.domain.PendingRegistrationStatus

@Entity(tableName = "educational_levels_cache")
data class EducationalLevelCacheEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val maxGrade: Int,
    val isActive: Boolean
)

@Entity(tableName = "schools_cache")
data class SchoolCacheEntity(
    @PrimaryKey val id: Int,
    val educationalLevelId: Int,
    val educationalLevelName: String?,
    val name: String,
    val isActive: Boolean
)

@Entity(tableName = "school_groups_cache")
data class SchoolGroupCacheEntity(
    @PrimaryKey val id: Int,
    val schoolId: Int,
    val schoolName: String?,
    val educationalLevelId: Int?,
    val educationalLevelName: String?,
    val groupCode: String,
    val isActive: Boolean
)

@Entity(tableName = "product_types_cache")
data class ProductTypeCacheEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val requiresSize: Boolean,
    val requiresFinish: Boolean,
    val requiresFrameModel: Boolean,
    val requiresColor: Boolean,
    val allowedSizeGroup: String?,
    val isActive: Boolean
)

@Entity(tableName = "finishes_cache")
data class FinishCacheEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val isActive: Boolean
)

@Entity(tableName = "sizes_cache")
data class SizeCacheEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val sizeGroup: String,
    val isActive: Boolean
)

@Entity(
    tableName = "frame_model_finish_relations_cache",
    primaryKeys = ["frameModelId", "finishId"]
)
data class FrameModelFinishRelationCacheEntity(
    val frameModelId: Int,
    val frameModelName: String,
    val finishId: Int,
    val finishName: String
)

@Entity(
    tableName = "frame_model_finish_color_relations_cache",
    primaryKeys = ["frameModelId", "finishId", "colorId"]
)
data class FrameModelFinishColorRelationCacheEntity(
    val frameModelId: Int,
    val frameModelName: String,
    val finishId: Int,
    val finishName: String,
    val colorId: Int,
    val colorName: String,
    val colorHex: String?
)

@Entity(tableName = "price_rules_cache")
data class PriceRuleCacheEntity(
    @PrimaryKey val id: Int,
    val productTypeId: Int,
    val productTypeName: String,
    val finishId: Int,
    val finishName: String,
    val sizeId: Int,
    val sizeName: String,
    val sizeGroup: String,
    val price: Double,
    val isActive: Boolean
)

@Entity(tableName = "pending_registrations")
data class PendingRegistrationEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0L,
    val payloadJson: String,
    val studentFullName: String,
    val schoolLabel: String,
    val productTypeName: String,
    val createdAt: Long,
    val status: PendingRegistrationStatus,
    val lastErrorMessage: String?,
    val lastAttemptAt: Long?,
    val syncedOrderId: Int?
)
