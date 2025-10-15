package com.teddy.mirandaytoledo.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teddy.mirandaytoledo.core.presentation.CountScreen
import com.teddy.mirandaytoledo.core.presentation.FinanceScreen
import com.teddy.mirandaytoledo.core.presentation.RegisterScreen
import com.teddy.mirandaytoledo.core.presentation.SearchScreen
import com.teddy.mirandaytoledo.core.presentation.SettingsScreen
import com.teddy.mirandaytoledo.core.presentation.StatusScreen
import com.teddy.mirandaytoledo.core.presentation.components.MainScaffold
import com.teddy.mirandaytoledo.home.presentation.HomeScreen

fun NavGraphBuilder.mainGraph(onLogout: () -> Unit) {
    composable<Home> {
        MainScaffold(onLogout = onLogout) {
            HomeScreen()
        }
    }
    composable<Register> {
        RegisterScreen()
    }

    composable<Search> {
        SearchScreen()
    }

    composable<Status> {
        StatusScreen()
    }
    composable<Finance> {
        FinanceScreen()
    }
    composable<Count> {
        CountScreen()
    }
    composable<Settings> {
        SettingsScreen()
    }
}
