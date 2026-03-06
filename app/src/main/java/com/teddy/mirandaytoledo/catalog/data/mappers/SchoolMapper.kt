package com.teddy.mirandaytoledo.catalog.data.mappers

import com.teddy.mirandaytoledo.catalog.data.dto.SchoolDto
import com.teddy.mirandaytoledo.catalog.domain.School

fun SchoolDto.toDomain(): School {
    return School(
        id = id,
        educationalLevelId = educationalLevelId,
        educationalLevelName = educationalLevelName,
        name = name,
        isActive = isActive
    )
}
