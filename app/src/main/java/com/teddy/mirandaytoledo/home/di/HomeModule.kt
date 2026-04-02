package com.teddy.mirandaytoledo.home.di

import com.teddy.mirandaytoledo.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeViewModel)
}
