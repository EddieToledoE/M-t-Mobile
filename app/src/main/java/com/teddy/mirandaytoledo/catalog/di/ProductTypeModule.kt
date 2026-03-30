package com.teddy.mirandaytoledo.catalog.di

import com.teddy.mirandaytoledo.catalog.prices.producttypes.data.networking.ProductTypeRemoteDataSource
import com.teddy.mirandaytoledo.catalog.prices.producttypes.data.repository.ProductTypeRepositoryImpl
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductTypeRepository
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.usecase.CreateProductTypeUseCase
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.usecase.DeleteProductTypeUseCase
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.usecase.GetProductTypesUseCase
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.usecase.UpdateProductTypeUseCase
import com.teddy.mirandaytoledo.catalog.prices.producttypes.presentation.ProductTypesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val productTypeModule = module {
    singleOf(::ProductTypeRemoteDataSource)
    singleOf(::ProductTypeRepositoryImpl).bind<ProductTypeRepository>()
    factoryOf(::GetProductTypesUseCase)
    factoryOf(::CreateProductTypeUseCase)
    factoryOf(::UpdateProductTypeUseCase)
    factoryOf(::DeleteProductTypeUseCase)
    viewModelOf(::ProductTypesViewModel)
}
