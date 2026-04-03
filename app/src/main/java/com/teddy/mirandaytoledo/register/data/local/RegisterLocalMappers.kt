package com.teddy.mirandaytoledo.register.data.local

import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.Finish
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishColorRelation
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelation
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.Size
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.PriceRule
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductType
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.EducationalLevel
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.SchoolGroup
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.School
import com.teddy.mirandaytoledo.register.domain.PendingRegistration

fun EducationalLevel.toCache() = EducationalLevelCacheEntity(
    id = id,
    name = name,
    maxGrade = maxGrade,
    isActive = isActive
)

fun EducationalLevelCacheEntity.toDomain() = EducationalLevel(
    id = id,
    name = name,
    maxGrade = maxGrade,
    isActive = isActive
)

fun School.toCache() = SchoolCacheEntity(
    id = id,
    educationalLevelId = educationalLevelId,
    educationalLevelName = educationalLevelName,
    name = name,
    isActive = isActive
)

fun SchoolCacheEntity.toDomain() = School(
    id = id,
    educationalLevelId = educationalLevelId,
    educationalLevelName = educationalLevelName,
    name = name,
    isActive = isActive
)

fun SchoolGroup.toCache() = SchoolGroupCacheEntity(
    id = id,
    schoolId = schoolId,
    schoolName = schoolName,
    educationalLevelId = educationalLevelId,
    educationalLevelName = educationalLevelName,
    groupCode = groupCode,
    isActive = isActive
)

fun SchoolGroupCacheEntity.toDomain() = SchoolGroup(
    id = id,
    schoolId = schoolId,
    schoolName = schoolName,
    educationalLevelId = educationalLevelId,
    educationalLevelName = educationalLevelName,
    groupCode = groupCode,
    isActive = isActive
)

fun ProductType.toCache() = ProductTypeCacheEntity(
    id = id,
    name = name,
    requiresSize = requiresSize,
    requiresFinish = requiresFinish,
    requiresFrameModel = requiresFrameModel,
    requiresColor = requiresColor,
    allowedSizeGroup = allowedSizeGroup,
    isActive = isActive
)

fun ProductTypeCacheEntity.toDomain() = ProductType(
    id = id,
    name = name,
    requiresSize = requiresSize,
    requiresFinish = requiresFinish,
    requiresFrameModel = requiresFrameModel,
    requiresColor = requiresColor,
    allowedSizeGroup = allowedSizeGroup,
    isActive = isActive
)

fun Finish.toCache() = FinishCacheEntity(id = id, name = name, isActive = isActive)

fun FinishCacheEntity.toDomain() = Finish(id = id, name = name, isActive = isActive)

fun Size.toCache() = SizeCacheEntity(id = id, name = name, sizeGroup = sizeGroup, isActive = isActive)

fun SizeCacheEntity.toDomain() = Size(id = id, name = name, sizeGroup = sizeGroup, isActive = isActive)

fun FrameModelFinishRelation.toCache() = FrameModelFinishRelationCacheEntity(
    frameModelId = frameModelId,
    frameModelName = frameModelName,
    finishId = finishId,
    finishName = finishName
)

fun FrameModelFinishRelationCacheEntity.toDomain() = FrameModelFinishRelation(
    frameModelId = frameModelId,
    frameModelName = frameModelName,
    finishId = finishId,
    finishName = finishName
)

fun FrameModelFinishColorRelation.toCache() = FrameModelFinishColorRelationCacheEntity(
    frameModelId = frameModelId,
    frameModelName = frameModelName,
    finishId = finishId,
    finishName = finishName,
    colorId = colorId,
    colorName = colorName,
    colorHex = colorHex
)

fun FrameModelFinishColorRelationCacheEntity.toDomain() = FrameModelFinishColorRelation(
    frameModelId = frameModelId,
    frameModelName = frameModelName,
    finishId = finishId,
    finishName = finishName,
    colorId = colorId,
    colorName = colorName,
    colorHex = colorHex
)

fun PriceRule.toCache() = PriceRuleCacheEntity(
    id = id,
    productTypeId = productTypeId,
    productTypeName = productTypeName,
    finishId = finishId,
    finishName = finishName,
    sizeId = sizeId,
    sizeName = sizeName,
    sizeGroup = sizeGroup,
    price = price,
    isActive = isActive
)

fun PriceRuleCacheEntity.toDomain() = PriceRule(
    id = id,
    productTypeId = productTypeId,
    productTypeName = productTypeName,
    finishId = finishId,
    finishName = finishName,
    sizeId = sizeId,
    sizeName = sizeName,
    sizeGroup = sizeGroup,
    price = price,
    isActive = isActive
)

fun PendingRegistrationEntity.toDomain() = PendingRegistration(
    localId = localId,
    payloadJson = payloadJson,
    studentFullName = studentFullName,
    schoolLabel = schoolLabel,
    productTypeName = productTypeName,
    createdAt = createdAt,
    status = status,
    lastErrorMessage = lastErrorMessage,
    lastAttemptAt = lastAttemptAt,
    syncedOrderId = syncedOrderId
)
