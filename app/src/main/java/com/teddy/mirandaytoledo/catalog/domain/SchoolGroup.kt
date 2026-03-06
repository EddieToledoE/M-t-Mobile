package com.teddy.mirandaytoledo.catalog.domain

data class SchoolGroup(
    val id: Int,
    val schoolId: Int,
    val groupCode: String,
    val isActive: Boolean
)
