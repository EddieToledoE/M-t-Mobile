package com.teddy.mirandaytoledo.catalog.di

import com.teddy.mirandaytoledo.register.data.networking.RegisterRemoteDataSource
import com.teddy.mirandaytoledo.register.data.repository.RegisterRepositoryImpl
import com.teddy.mirandaytoledo.register.domain.RegisterRepository
import com.teddy.mirandaytoledo.register.domain.usecase.SubmitOrderRegistrationUseCase
import com.teddy.mirandaytoledo.register.presentation.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val registerModule = module {
    singleOf(::RegisterRemoteDataSource)
    singleOf(::RegisterRepositoryImpl).bind<RegisterRepository>()
    factoryOf(::SubmitOrderRegistrationUseCase)
    viewModelOf(::RegisterViewModel)
}
