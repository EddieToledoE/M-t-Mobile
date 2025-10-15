package com.teddy.mirandaytoledo.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.teddy.mirandaytoledo.auth.presentation.login.LoginScreen
import com.teddy.mirandaytoledo.home.presentation.HomeScreen

@Composable
fun NavigationWrapper(modifier: Modifier) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {
        composable<Login> { LoginScreen(onSuccessLogin = { navController.navigate(Home) }) }
        composable<Home> { HomeScreen() }
    }
}