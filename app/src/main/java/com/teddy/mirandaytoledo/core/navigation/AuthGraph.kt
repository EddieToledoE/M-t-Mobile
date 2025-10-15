package com.teddy.mirandaytoledo.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teddy.mirandaytoledo.auth.presentation.login.LoginScreen

fun NavGraphBuilder.authGraph(onLoginSuccess: () -> Unit) {
    composable<Login> {
        LoginScreen(onSuccessLogin = onLoginSuccess)
    }
}
