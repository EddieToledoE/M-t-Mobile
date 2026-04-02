package com.teddy.mirandaytoledo.di

import androidx.room.Room
import com.teddy.mirandaytoledo.auth.data.networking.RemoteLoginDataSource
import com.teddy.mirandaytoledo.auth.domain.LoginDataSource
import com.teddy.mirandaytoledo.core.data.networking.HttpClientFactory
import com.teddy.mirandaytoledo.auth.presentation.login.LoginViewModel
import com.teddy.mirandaytoledo.core.navigation.Navigator
import com.teddy.mirandaytoledo.register.data.local.RegisterDatabase
import io.ktor.client.engine.cio.CIO
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { Navigator() }
    single { HttpClientFactory.create(engine = CIO.create()) }
    single {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }
    single {
        Room.databaseBuilder(
            get(),
            RegisterDatabase::class.java,
            "register_offline.db"
        ).fallbackToDestructiveMigration().build()
    }
    single { get<RegisterDatabase>().registerCatalogDao() }
    single { get<RegisterDatabase>().pendingRegistrationDao() }
    singleOf(::RemoteLoginDataSource).bind<LoginDataSource>()
    viewModelOf(::LoginViewModel)
}
