package com.teddy.mirandaytoledo.core.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.FilterFrames
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AppRegistration
import androidx.compose.material.icons.outlined.FilterFrames
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.core.navigation.Count
import com.teddy.mirandaytoledo.core.navigation.Finance
import com.teddy.mirandaytoledo.core.navigation.Home
import com.teddy.mirandaytoledo.core.navigation.Register
import com.teddy.mirandaytoledo.core.navigation.Search
import com.teddy.mirandaytoledo.core.navigation.Settings
import com.teddy.mirandaytoledo.core.navigation.Status

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    onSelectedItem: (Any) -> Unit,
) {
    val items = listOf(
        NavigationItem(
            textResId = R.string.navdraw_title_home,
            unselectedIcon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home,
            destination = Home
        ),
        NavigationItem(
            textResId = R.string.navdraw_title_register,
            unselectedIcon = Icons.Outlined.AppRegistration,
            selectedIcon = Icons.Filled.AppRegistration,
            destination = Register
        ),
        NavigationItem(
            textResId = R.string.navdraw_title_search,
            unselectedIcon = Icons.Outlined.PersonSearch,
            selectedIcon = Icons.Filled.PersonSearch,
            destination = Search
        ),
        NavigationItem(
            textResId = R.string.navdraw_title_status,
            unselectedIcon = Icons.Outlined.FilterFrames,
            selectedIcon = Icons.Filled.FilterFrames,
            destination = Status
        ),
        NavigationItem(
            textResId = R.string.navdraw_title_finances,
            unselectedIcon = Icons.Outlined.MonetizationOn,
            selectedIcon = Icons.Filled.MonetizationOn,
            destination = Finance
        ),
        NavigationItem(
            textResId = R.string.navdraw_title_count,
            unselectedIcon = Icons.Outlined.Numbers,
            selectedIcon = Icons.Filled.Numbers,
            destination = Count
        ),
        NavigationItem(
            textResId = R.string.navdraw_title_settings,
            unselectedIcon = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings,
            destination = Settings
        ),
        NavigationItem(
            textResId = R.string.navdraw_title_logout,
            unselectedIcon = Icons.AutoMirrored.Outlined.Logout,
            selectedIcon = Icons.AutoMirrored.Filled.Logout,
            destination = null
        )
    )
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    ModalDrawerSheet() {
        Spacer(modifier = modifier.height(16.dp))
        items.forEachIndexed { index, item ->
            NavigationDrawerItem(
                label = { Text(text = stringResource(item.textResId)) },
                selected = index == selectedItemIndex,
                onClick = {
                    if (item.textResId == R.string.navdraw_title_logout) {
                        onLogout()
                    } else {
                        selectedItemIndex = index
                        item.destination?.let(onSelectedItem)
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon,
                        contentDescription = stringResource(item.textResId)
                    )
                },
                modifier = modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }

}


data class NavigationItem(
    @StringRes val textResId: Int,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val destination: Any? = null,
)