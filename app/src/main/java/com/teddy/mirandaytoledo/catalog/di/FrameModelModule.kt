package com.teddy.mirandaytoledo.catalog.di

import com.teddy.mirandaytoledo.catalog.frames.models.data.networking.FrameModelRemoteDataSource
import com.teddy.mirandaytoledo.catalog.frames.models.data.repository.FrameModelRepositoryImpl
import com.teddy.mirandaytoledo.catalog.frames.models.domain.FrameModelRepository
import com.teddy.mirandaytoledo.catalog.frames.models.domain.usecase.CreateFrameModelUseCase
import com.teddy.mirandaytoledo.catalog.frames.models.domain.usecase.DeleteFrameModelUseCase
import com.teddy.mirandaytoledo.catalog.frames.models.domain.usecase.GetFrameModelsUseCase
import com.teddy.mirandaytoledo.catalog.frames.models.domain.usecase.UpdateFrameModelUseCase
import com.teddy.mirandaytoledo.catalog.frames.models.presentation.FrameModelsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val frameModelModule = module {
    singleOf(::FrameModelRemoteDataSource)
    singleOf(::FrameModelRepositoryImpl).bind<FrameModelRepository>()
    factoryOf(::GetFrameModelsUseCase)
    factoryOf(::CreateFrameModelUseCase)
    factoryOf(::UpdateFrameModelUseCase)
    factoryOf(::DeleteFrameModelUseCase)
    viewModelOf(::FrameModelsViewModel)
}
