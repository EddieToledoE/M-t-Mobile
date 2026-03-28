package com.teddy.mirandaytoledo.catalog.scholar.groups.domain

data class SchoolGroup(
    val id: Int,
    val schoolId: Int,
    val schoolName: String?,
    val educationalLevelId: Int?,
    val educationalLevelName: String?,
    val groupCode: String,
    val isActive: Boolean
)
