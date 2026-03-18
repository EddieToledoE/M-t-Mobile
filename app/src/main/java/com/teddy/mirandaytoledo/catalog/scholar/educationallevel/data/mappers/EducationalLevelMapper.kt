package com.teddy.mirandaytoledo.catalog.scholar.educationallevel.data.mappers

import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.data.dto.EducationalLevelDto
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.EducationalLevel

fun EducationalLevelDto.toDomain(): EducationalLevel {
    return EducationalLevel(
        id = id,
        name = name,
        maxGrade = maxGrade,
        isActive = isActive
    )
}
