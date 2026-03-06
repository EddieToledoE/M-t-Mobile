package com.teddy.mirandaytoledo.catalog.data.mappers

import com.teddy.mirandaytoledo.catalog.data.dto.SchoolGroupDto
import com.teddy.mirandaytoledo.catalog.domain.SchoolGroup

fun SchoolGroupDto.toDomain(): SchoolGroup {
    return SchoolGroup(
        id = id,
        schoolId = schoolId,
        groupCode = groupCode,
        isActive = isActive
    )
}
