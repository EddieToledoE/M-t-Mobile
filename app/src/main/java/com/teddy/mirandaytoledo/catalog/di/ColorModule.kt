package com.teddy.mirandaytoledo.catalog.di

import com.teddy.mirandaytoledo.catalog.frames.colors.data.networking.CatalogColorRemoteDataSource
import com.teddy.mirandaytoledo.catalog.frames.colors.data.repository.CatalogColorRepositoryImpl
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.CatalogColorRepository
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.usecase.CreateCatalogColorUseCase
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.usecase.DeleteCatalogColorUseCase
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.usecase.GetCatalogColorsUseCase
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.usecase.UpdateCatalogColorUseCase
import com.teddy.mirandaytoledo.catalog.frames.colors.presentation.CatalogColorsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val colorModule = module {
    singleOf(::CatalogColorRemoteDataSource)
    singleOf(::CatalogColorRepositoryImpl).bind<CatalogColorRepository>()
    factoryOf(::GetCatalogColorsUseCase)
    factoryOf(::CreateCatalogColorUseCase)
    factoryOf(::UpdateCatalogColorUseCase)
    factoryOf(::DeleteCatalogColorUseCase)
    viewModelOf(::CatalogColorsViewModel)
}
