package com.teddy.mirandaytoledo.catalog.di

import com.teddy.mirandaytoledo.catalog.prices.pricerules.data.networking.PriceRuleRemoteDataSource
import com.teddy.mirandaytoledo.catalog.prices.pricerules.data.repository.PriceRuleRepositoryImpl
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.PriceRuleRepository
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.usecase.CreatePriceRuleUseCase
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.usecase.DeletePriceRuleUseCase
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.usecase.GetPriceRulesUseCase
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.usecase.UpdatePriceRuleUseCase
import com.teddy.mirandaytoledo.catalog.prices.pricerules.presentation.PriceRulesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val priceRuleModule = module {
    singleOf(::PriceRuleRemoteDataSource)
    singleOf(::PriceRuleRepositoryImpl).bind<PriceRuleRepository>()
    factoryOf(::GetPriceRulesUseCase)
    factoryOf(::CreatePriceRuleUseCase)
    factoryOf(::UpdatePriceRuleUseCase)
    factoryOf(::DeletePriceRuleUseCase)
    viewModelOf(::PriceRulesViewModel)
}
