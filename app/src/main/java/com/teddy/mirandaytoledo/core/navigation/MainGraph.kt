package com.teddy.mirandaytoledo.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teddy.mirandaytoledo.core.presentation.CountScreen
import com.teddy.mirandaytoledo.core.presentation.FinanceScreen
import com.teddy.mirandaytoledo.register.presentation.RegisterScreen
import com.teddy.mirandaytoledo.core.presentation.SearchScreen
import com.teddy.mirandaytoledo.core.presentation.SettingsScreen
import com.teddy.mirandaytoledo.core.presentation.StatusScreen
import com.teddy.mirandaytoledo.core.presentation.components.MainScaffold
import com.teddy.mirandaytoledo.home.presentation.HomeScreen

fun NavGraphBuilder.mainGraph(onLogout: () -> Unit) {
    composable<Home> {
        MainScaffold(onLogout = onLogout) { HomeScreen() }
    }
    composable<Register> {
        MainScaffold(onLogout = onLogout) { RegisterScreen() }
    }

    composable<Search> {
        MainScaffold(onLogout = onLogout) { SearchScreen() }
    }

    composable<Status> {
        MainScaffold(onLogout = onLogout) { StatusScreen() }
    }

    composable<Finance> {
        MainScaffold(onLogout = onLogout) { FinanceScreen() }
    }

    composable<Count> {
        MainScaffold(onLogout = onLogout) { CountScreen() }
    }

    composable<Settings> {
        MainScaffold(onLogout = onLogout) { SettingsScreen() }
    }
}
