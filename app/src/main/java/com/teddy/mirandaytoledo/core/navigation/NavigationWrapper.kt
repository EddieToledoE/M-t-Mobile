package com.teddy.mirandaytoledo.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationWrapper(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Login
    ) {

        authGraph(
            onLoginSuccess = {
                navController.navigate(Home) {
                    popUpTo(Login) { inclusive = true }
                }
            }
        )

        mainGraph(
            onLogout = {
                navController.navigate(Login) {
                    popUpTo(Home) { inclusive = true }
                }
            }
        )

    }
}
