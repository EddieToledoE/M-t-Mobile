package com.teddy.mirandaytoledo.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.teddy.mirandaytoledo.catalog.presentation.CatalogScreen
import com.teddy.mirandaytoledo.catalog.presentation.EducationalLevelScreen
import com.teddy.mirandaytoledo.catalog.presentation.SchoolScreen
import com.teddy.mirandaytoledo.catalog.presentation.SchoolGroupScreen
import com.teddy.mirandaytoledo.catalog.presentation.FrameFinishScreen
import com.teddy.mirandaytoledo.catalog.presentation.FrameSizeScreen
import com.teddy.mirandaytoledo.catalog.presentation.FrameModelScreen
import com.teddy.mirandaytoledo.catalog.presentation.FrameColorScreen
import com.teddy.mirandaytoledo.catalog.presentation.FrameRestrictionScreen
import com.teddy.mirandaytoledo.catalog.presentation.ProductTypesScreen
import com.teddy.mirandaytoledo.catalog.presentation.PricesRulesScreen
import com.teddy.mirandaytoledo.catalog.presentation.components.ScholarCatalogChooser
import com.teddy.mirandaytoledo.catalog.presentation.components.FrameCatalogsChooser
import com.teddy.mirandaytoledo.catalog.presentation.components.PricesCatalogsChooser
import com.teddy.mirandaytoledo.core.presentation.components.MainScaffold

fun NavGraphBuilder.catalogGraph(navigator: Navigator, onLogout: () -> Unit) {
    navigation<CatalogGraphRoute>(startDestination = Catalog) {
        composable<Catalog> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) {
                CatalogScreen(
                    onNavigateToScholarCatalog = { navigator.navigateTo(ScholarCatalog) },
                    onNavigateToFrameCatalog = { navigator.navigateTo(FrameCatalog) },
                    onNavigateToPricesCatalog = { navigator.navigateTo(PricesCatalog) }
                )
            }
        }
        composable<ScholarCatalog> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) {
                ScholarCatalogChooser(
                    onNavigateToEducationalLevel = { navigator.navigateTo(EducationalLevel) },
                    onNavigateToSchools = { navigator.navigateTo(School) },
                    onNavigateToGroups = { navigator.navigateTo(SchoolGroup) }
                )
            }
        }
        composable<EducationalLevel> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) { EducationalLevelScreen() }
        }
        composable<School> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) { SchoolScreen() }
        }
        composable<SchoolGroup> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) { SchoolGroupScreen() }
        }

        composable<FrameCatalog> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) {
                FrameCatalogsChooser(
                    onNavigateToFinishes = { navigator.navigateTo(FrameFinish) },
                    onNavigateToSizes = { navigator.navigateTo(FrameSize) },
                    onNavigateToModels = { navigator.navigateTo(FrameModel) },
                    onNavigateToColors = { navigator.navigateTo(FrameColor) },
                    onNavigateToRestrictions = { navigator.navigateTo(FrameRestriction) }
                )
            }
        }
        composable<FrameFinish> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) { FrameFinishScreen() }
        }
        composable<FrameSize> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) { FrameSizeScreen() }
        }
        composable<FrameModel> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) { FrameModelScreen() }
        }
        composable<FrameColor> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) { FrameColorScreen() }
        }
        composable<FrameRestriction> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) { FrameRestrictionScreen() }
        }

        composable<PricesCatalog> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) {
                PricesCatalogsChooser(
                    onNavigateToProductTypes = { navigator.navigateTo(ProductTypes) },
                    onNavigateToPricesRules = { navigator.navigateTo(PricesRules) }
                )
            }
        }
        composable<ProductTypes> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) { ProductTypesScreen() }
        }
        composable<PricesRules> {
            MainScaffold(currentRoute = Catalog, onLogout = onLogout) { PricesRulesScreen() }
        }
    }
}
