package com.teddy.mirandaytoledo.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teddy.mirandaytoledo.catalog.presentation.CatalogScreen
import com.teddy.mirandaytoledo.catalog.presentation.components.ScholarCatalogChooser
import com.teddy.mirandaytoledo.core.presentation.CountScreen
import com.teddy.mirandaytoledo.core.presentation.FinanceScreen
import com.teddy.mirandaytoledo.register.presentation.RegisterScreen
import com.teddy.mirandaytoledo.core.presentation.SearchScreen
import com.teddy.mirandaytoledo.core.presentation.SettingsScreen
import com.teddy.mirandaytoledo.core.presentation.StatusScreen
import com.teddy.mirandaytoledo.core.presentation.components.MainScaffold
import com.teddy.mirandaytoledo.home.presentation.HomeScreen

fun NavGraphBuilder.mainGraph(navigator: Navigator, onLogout: () -> Unit) {
    composable<Home> {
        MainScaffold(currentRoute = Home, onLogout = onLogout) { HomeScreen() }
    }
    composable<Register> {
        MainScaffold(currentRoute = Register, onLogout = onLogout) { RegisterScreen() }
    }

    composable<Search> {
        MainScaffold(currentRoute = Search, onLogout = onLogout) { SearchScreen() }
    }

    composable<Status> {
        MainScaffold(currentRoute = Status, onLogout = onLogout) { StatusScreen() }
    }

    composable<Finance> {
        MainScaffold(currentRoute = Finance, onLogout = onLogout) { FinanceScreen() }
    }

    composable<Count> {
        MainScaffold(currentRoute = Count, onLogout = onLogout) { CountScreen() }
    }

    composable<Settings> {
        MainScaffold(currentRoute = Settings, onLogout = onLogout) { SettingsScreen() }
    }
    catalogGraph(navigator = navigator, onLogout = onLogout)
}
