package com.teddy.mirandaytoledo.catalog.di

import com.teddy.mirandaytoledo.catalog.scholar.schools.data.networking.SchoolRemoteDataSource
import com.teddy.mirandaytoledo.catalog.scholar.schools.data.repository.SchoolRepositoryImpl
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.SchoolRepository
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase.CreateSchoolUseCase
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase.DeleteSchoolUseCase
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase.GetSchoolsByLevelUseCase
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase.GetSchoolsUseCase
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase.UpdateSchoolUseCase
import com.teddy.mirandaytoledo.catalog.scholar.schools.presentation.SchoolsViewModel
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
