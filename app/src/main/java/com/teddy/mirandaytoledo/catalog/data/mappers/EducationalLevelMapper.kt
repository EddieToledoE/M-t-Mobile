package com.teddy.mirandaytoledo.catalog.data.mappers

import com.teddy.mirandaytoledo.catalog.data.dto.EducationalLevelDto
import com.teddy.mirandaytoledo.catalog.domain.EducationalLevel

fun EducationalLevelDto.toDomain(): EducationalLevel {
    return EducationalLevel(
        id = id,
        name = name,
        maxGrade = maxGrade,
        isActive = isActive
    )
}
