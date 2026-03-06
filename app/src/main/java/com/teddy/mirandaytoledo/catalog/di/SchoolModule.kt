package com.teddy.mirandaytoledo.catalog.di

import com.teddy.mirandaytoledo.catalog.data.networking.SchoolRemoteDataSource
import com.teddy.mirandaytoledo.catalog.data.repository.SchoolRepositoryImpl
import com.teddy.mirandaytoledo.catalog.domain.SchoolRepository
import com.teddy.mirandaytoledo.catalog.domain.usecase.CreateSchoolUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.DeleteSchoolUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.GetSchoolsByLevelUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.GetSchoolsUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.UpdateSchoolUseCase
import com.teddy.mirandaytoledo.catalog.presentation.schools.SchoolsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val schoolModule = module {
    singleOf(::SchoolRemoteDataSource)
    singleOf(::SchoolRepositoryImpl).bind<SchoolRepository>()
    factoryOf(::GetSchoolsUseCase)
    factoryOf(::GetSchoolsByLevelUseCase)
    factoryOf(::CreateSchoolUseCase)
    factoryOf(::UpdateSchoolUseCase)
    factoryOf(::DeleteSchoolUseCase)
    viewModelOf(::SchoolsViewModel)
}
