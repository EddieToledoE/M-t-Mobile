package com.teddy.mirandaytoledo.catalog.di

import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.data.networking.EducationalLevelRemoteDataSource
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.data.repository.EducationalLevelRepositoryImpl
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.EducationalLevelRepository
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.usecase.CreateEducationalLevelUseCase
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.usecase.DeleteEducationalLevelUseCase
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.usecase.GetEducationalLevelsUseCase
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.usecase.UpdateEducationalLevelUseCase
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.presentation.EducationalLevelsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val educationalLevelModule = module {
    singleOf(::EducationalLevelRemoteDataSource)
    singleOf(::EducationalLevelRepositoryImpl).bind<EducationalLevelRepository>()
    factoryOf(::GetEducationalLevelsUseCase)
    factoryOf(::CreateEducationalLevelUseCase)
    factoryOf(::UpdateEducationalLevelUseCase)
    factoryOf(::DeleteEducationalLevelUseCase)
    viewModelOf(::EducationalLevelsViewModel)
}
