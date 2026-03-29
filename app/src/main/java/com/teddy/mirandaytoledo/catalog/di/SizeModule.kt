package com.teddy.mirandaytoledo.catalog.di

import com.teddy.mirandaytoledo.catalog.frames.sizes.data.networking.SizeRemoteDataSource
import com.teddy.mirandaytoledo.catalog.frames.sizes.data.repository.SizeRepositoryImpl
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.SizeRepository
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.usecase.CreateSizeUseCase
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.usecase.DeleteSizeUseCase
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.usecase.GetSizesUseCase
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.usecase.UpdateSizeUseCase
import com.teddy.mirandaytoledo.catalog.frames.sizes.presentation.SizesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sizeModule = module {
    singleOf(::SizeRemoteDataSource)
    singleOf(::SizeRepositoryImpl).bind<SizeRepository>()
    factoryOf(::GetSizesUseCase)
    factoryOf(::CreateSizeUseCase)
    factoryOf(::UpdateSizeUseCase)
    factoryOf(::DeleteSizeUseCase)
    viewModelOf(::SizesViewModel)
}
