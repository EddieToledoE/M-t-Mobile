package com.teddy.mirandaytoledo.core.navigation

import androidx.appcompat.widget.PopupMenu
import androidx.navigation.NavController

class Navigator {
    private var navController: NavController? = null


    fun setController(controller: NavController) {
        navController = controller
    }

    fun navigateTo(route: Any) {
        navController?.navigate(route)
    }

    fun navigateAndClear(route: Any, popUpTo: Any) {
        navController?.navigate(route) {
            popUpTo(popUpTo) { inclusive = true }
        }
    }

    fun goBack() {
        navController?.navigateUp()
    }
}