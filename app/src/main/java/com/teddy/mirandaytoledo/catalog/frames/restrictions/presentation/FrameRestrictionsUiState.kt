package com.teddy.mirandaytoledo.catalog.frames.restrictions.presentation

import com.teddy.mirandaytoledo.catalog.frames.colors.domain.CatalogColor
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.Finish
import com.teddy.mirandaytoledo.catalog.frames.models.domain.FrameModel
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishColorRelation
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelation
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

data class FrameRestrictionsUiState(
    val isLoading: Boolean = true,
    val isColorSectionLoading: Boolean = false,
    val combinations: List<FrameModelFinishRelation> = emptyList(),
    val selectedCombination: FrameModelFinishRelation? = null,
    val allowedColors: List<FrameModelFinishColorRelation> = emptyList(),
    val frameModels: List<FrameModel> = emptyList(),
    val finishes: List<Finish> = emptyList(),
    val colors: List<CatalogColor> = emptyList(),
    val error: NetworkError? = null
)
