package com.teddy.mirandaytoledo.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import org.koin.compose.getKoin

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val navigator: Navigator = getKoin().get()

    LaunchedEffect(navController) {
        navigator.setController(navController)
    }
    NavHost(
        navController = navController,
        startDestination = Login,
    ) {

        authGraph(
            onLoginSuccess = {
                navigator.navigateAndClear(route = Home, popUpTo = Login)
            }
        )

        mainGraph(
            navigator = navigator,
            onLogout = {
                navigator.navigateAndClear(route = Login, popUpTo = Home)
            }
        )

    }
}
