package com.teddy.mirandaytoledo.catalog.scholar.schools.data.mappers

import com.teddy.mirandaytoledo.catalog.scholar.schools.data.dto.SchoolDto
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.School

fun SchoolDto.toDomain(): School {
    return School(
        id = id,
        educationalLevelId = educationalLevelId,
        educationalLevelName = educationalLevelName,
        name = name,
        isActive = isActive
    )
}
