package com.teddy.mirandaytoledo.catalog.scholar.groups.domain

data class SchoolGroup(
    val id: Int,
    val schoolId: Int,
    val groupCode: String,
    val isActive: Boolean
)
