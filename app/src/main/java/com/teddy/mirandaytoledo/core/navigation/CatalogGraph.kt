package com.teddy.mirandaytoledo.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.teddy.mirandaytoledo.catalog.presentation.CatalogScreen
import com.teddy.mirandaytoledo.catalog.presentation.EducationalLevelScreen
import com.teddy.mirandaytoledo.catalog.presentation.components.ScholarCatalogChooser
import com.teddy.mirandaytoledo.core.presentation.components.MainScaffold

fun NavGraphBuilder.catalogGraph(navigator: Navigator, onLogout: () -> Unit) {
    navigation<CatalogGraphRoute>(startDestination = Catalog) {
        composable<Catalog> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) {
                CatalogScreen(
                    onNavigateToScholarCatalog = { navigator.navigateTo(ScholarCatalog) }
                )
            }
        }
        composable<ScholarCatalog> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) {
                ScholarCatalogChooser(
                    onNavigateToEducationalLevel = { navigator.navigateTo(EducationalLevel) }
                )
            }
        }
        composable<EducationalLevel> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) {
                EducationalLevelScreen()
            }
        }
    }
}
