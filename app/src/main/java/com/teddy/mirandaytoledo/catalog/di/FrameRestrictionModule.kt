package com.teddy.mirandaytoledo.catalog.di

import com.teddy.mirandaytoledo.catalog.frames.restrictions.data.networking.FrameModelFinishColorRelationRemoteDataSource
import com.teddy.mirandaytoledo.catalog.frames.restrictions.data.networking.FrameModelFinishRelationRemoteDataSource
import com.teddy.mirandaytoledo.catalog.frames.restrictions.data.repository.FrameModelFinishColorRelationRepositoryImpl
import com.teddy.mirandaytoledo.catalog.frames.restrictions.data.repository.FrameModelFinishRelationRepositoryImpl
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishColorRelationRepository
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelationRepository
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.CreateFrameModelFinishColorRelationUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.CreateFrameModelFinishRelationUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.DeleteFrameModelFinishColorRelationUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.DeleteFrameModelFinishRelationUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.GetFrameModelFinishColorRelationsUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.GetFrameModelFinishRelationsUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.presentation.FrameRestrictionsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val frameRestrictionModule = module {
    singleOf(::FrameModelFinishRelationRemoteDataSource)
    singleOf(::FrameModelFinishColorRelationRemoteDataSource)
    singleOf(::FrameModelFinishRelationRepositoryImpl).bind<FrameModelFinishRelationRepository>()
    singleOf(::FrameModelFinishColorRelationRepositoryImpl).bind<FrameModelFinishColorRelationRepository>()
    factoryOf(::GetFrameModelFinishRelationsUseCase)
    factoryOf(::CreateFrameModelFinishRelationUseCase)
    factoryOf(::DeleteFrameModelFinishRelationUseCase)
    factoryOf(::GetFrameModelFinishColorRelationsUseCase)
    factoryOf(::CreateFrameModelFinishColorRelationUseCase)
    factoryOf(::DeleteFrameModelFinishColorRelationUseCase)
    viewModelOf(::FrameRestrictionsViewModel)
}
