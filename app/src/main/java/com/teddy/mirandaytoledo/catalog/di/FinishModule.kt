package com.teddy.mirandaytoledo.catalog.di

import com.teddy.mirandaytoledo.catalog.frames.finishes.data.networking.FinishRemoteDataSource
import com.teddy.mirandaytoledo.catalog.frames.finishes.data.repository.FinishRepositoryImpl
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.FinishRepository
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase.CreateFinishUseCase
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase.DeleteFinishUseCase
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase.GetFinishesUseCase
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase.UpdateFinishUseCase
import com.teddy.mirandaytoledo.catalog.frames.finishes.presentation.FinishesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val finishModule = module {
    singleOf(::FinishRemoteDataSource)
    singleOf(::FinishRepositoryImpl).bind<FinishRepository>()
    factoryOf(::GetFinishesUseCase)
    factoryOf(::CreateFinishUseCase)
    factoryOf(::UpdateFinishUseCase)
    factoryOf(::DeleteFinishUseCase)
    viewModelOf(::FinishesViewModel)
}
