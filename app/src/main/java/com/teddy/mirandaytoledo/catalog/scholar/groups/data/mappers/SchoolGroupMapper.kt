package com.teddy.mirandaytoledo.catalog.data.mappers

import com.teddy.mirandaytoledo.catalog.data.dto.SchoolGroupDto
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.SchoolGroup

fun SchoolGroupDto.toDomain(): SchoolGroup {
    return SchoolGroup(
        id = id,
        schoolId = schoolId,
        schoolName = schoolName,
        educationalLevelId = educationalLevelId,
        educationalLevelName = educationalLevelName,
        groupCode = groupCode,
        isActive = isActive
    )
}
