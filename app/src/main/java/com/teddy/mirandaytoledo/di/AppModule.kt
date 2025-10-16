package com.teddy.mirandaytoledo.di

import com.teddy.mirandaytoledo.auth.data.networking.RemoteLoginDataSource
import com.teddy.mirandaytoledo.auth.domain.LoginDataSource
import com.teddy.mirandaytoledo.core.data.networking.HttpClientFactory
import com.teddy.mirandaytoledo.auth.presentation.login.LoginViewModel
import com.teddy.mirandaytoledo.core.navigation.Navigator
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { Navigator() }
    single { HttpClientFactory.create(engine = CIO.create()) }
    singleOf(::RemoteLoginDataSource).bind<LoginDataSource>()
    viewModelOf(::LoginViewModel)
}