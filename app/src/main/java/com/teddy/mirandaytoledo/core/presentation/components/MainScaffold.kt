package com.teddy.mirandaytoledo.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.teddy.mirandaytoledo.home.presentation.components.TopBar
import kotlinx.coroutines.launch

@Composable
fun MainScaffold(
    navController: NavController,
    onLogout: () -> Unit,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onLogout = {
                    scope.launch { drawerState.close() }
                    onLogout()
                },
                onSelectedItem = { destination ->
                    scope.launch { drawerState.close() }
                    navController.navigate(destination)
                })
        }
    ) {
        Scaffold(
            topBar = { TopBar(onIconClick = { scope.launch { drawerState.open() } }) },
            floatingActionButton = {},
            bottomBar = {},

            ) { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                content()
            }
        }
    }

}
