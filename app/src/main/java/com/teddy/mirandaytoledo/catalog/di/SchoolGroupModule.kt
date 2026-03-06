package com.teddy.mirandaytoledo.catalog.di

import com.teddy.mirandaytoledo.catalog.data.networking.SchoolGroupRemoteDataSource
import com.teddy.mirandaytoledo.catalog.data.repository.SchoolGroupRepositoryImpl
import com.teddy.mirandaytoledo.catalog.domain.SchoolGroupRepository
import com.teddy.mirandaytoledo.catalog.domain.usecase.CreateSchoolGroupUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.DeleteSchoolGroupUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.GetSchoolGroupsBySchoolUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.GetSchoolGroupsUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.UpdateSchoolGroupUseCase
import com.teddy.mirandaytoledo.catalog.presentation.schoolgroups.SchoolGroupsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val schoolGroupModule = module {
    singleOf(::SchoolGroupRemoteDataSource)
    singleOf(::SchoolGroupRepositoryImpl).bind<SchoolGroupRepository>()
    factoryOf(::GetSchoolGroupsUseCase)
    factoryOf(::GetSchoolGroupsBySchoolUseCase)
    factoryOf(::CreateSchoolGroupUseCase)
    factoryOf(::UpdateSchoolGroupUseCase)
    factoryOf(::DeleteSchoolGroupUseCase)
    viewModelOf(::SchoolGroupsViewModel)
}
