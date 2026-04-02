package com.teddy.mirandaytoledo.register.domain

import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.Finish
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishColorRelation
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelation
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.Size
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.PriceRule
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductType
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.EducationalLevel
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.SchoolGroup
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.School

data class RegisterCatalogBundle(
    val educationalLevels: List<EducationalLevel> = emptyList(),
    val schools: List<School> = emptyList(),
    val schoolGroups: List<SchoolGroup> = emptyList(),
    val productTypes: List<ProductType> = emptyList(),
    val finishes: List<Finish> = emptyList(),
    val sizes: List<Size> = emptyList(),
    val frameModelFinishRelations: List<FrameModelFinishRelation> = emptyList(),
    val frameModelFinishColorRelations: List<FrameModelFinishColorRelation> = emptyList(),
    val priceRules: List<PriceRule> = emptyList()
)
