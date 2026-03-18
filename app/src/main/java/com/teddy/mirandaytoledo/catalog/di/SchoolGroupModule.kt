package com.teddy.mirandaytoledo.catalog.di

import com.teddy.mirandaytoledo.catalog.scholar.groups.data.networking.SchoolGroupRemoteDataSource
import com.teddy.mirandaytoledo.catalog.scholar.groups.data.repository.SchoolGroupRepositoryImpl
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.SchoolGroupRepository
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.CreateSchoolGroupUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.DeleteSchoolGroupUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.GetSchoolGroupsBySchoolUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.GetSchoolGroupsUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.UpdateSchoolGroupUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.presentation.SchoolGroupsViewModel
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
