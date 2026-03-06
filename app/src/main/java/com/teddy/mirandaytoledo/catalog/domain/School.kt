package com.teddy.mirandaytoledo.catalog.domain

data class School(
    val id: Int,
    val educationalLevelId: Int,
    val educationalLevelName: String?,
    val name: String,
    val isActive: Boolean
)
