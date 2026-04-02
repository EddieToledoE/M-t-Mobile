package com.teddy.mirandaytoledo.catalog.di

import com.teddy.mirandaytoledo.register.data.networking.RegisterRemoteDataSource
import com.teddy.mirandaytoledo.register.data.repository.RegisterOfflineRepositoryImpl
import com.teddy.mirandaytoledo.register.data.repository.RegisterRepositoryImpl
import com.teddy.mirandaytoledo.register.domain.RegisterOfflineRepository
import com.teddy.mirandaytoledo.register.domain.RegisterRepository
import com.teddy.mirandaytoledo.register.domain.usecase.DeletePendingRegistrationUseCase
import com.teddy.mirandaytoledo.register.domain.usecase.ObservePendingRegistrationsUseCase
import com.teddy.mirandaytoledo.register.domain.usecase.ObserveRegisterCatalogBundleUseCase
import com.teddy.mirandaytoledo.register.domain.usecase.SavePendingRegistrationUseCase
import com.teddy.mirandaytoledo.register.domain.usecase.SubmitOrderRegistrationUseCase
import com.teddy.mirandaytoledo.register.domain.usecase.SubmitPendingRegistrationUseCase
import com.teddy.mirandaytoledo.register.domain.usecase.SyncRegisterCatalogsUseCase
import com.teddy.mirandaytoledo.register.presentation.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val registerModule = module {
    singleOf(::RegisterRemoteDataSource)
    singleOf(::RegisterRepositoryImpl).bind<RegisterRepository>()
    singleOf(::RegisterOfflineRepositoryImpl).bind<RegisterOfflineRepository>()
    factoryOf(::ObserveRegisterCatalogBundleUseCase)
    factoryOf(::ObservePendingRegistrationsUseCase)
    factoryOf(::SyncRegisterCatalogsUseCase)
    factoryOf(::SavePendingRegistrationUseCase)
    factoryOf(::SubmitPendingRegistrationUseCase)
    factoryOf(::DeletePendingRegistrationUseCase)
    factoryOf(::SubmitOrderRegistrationUseCase)
    viewModelOf(::RegisterViewModel)
}
