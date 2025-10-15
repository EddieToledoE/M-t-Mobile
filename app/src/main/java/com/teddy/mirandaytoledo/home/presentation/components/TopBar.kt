@file:OptIn(ExperimentalMaterial3Api::class)

package com.teddy.mirandaytoledo.home.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.teddy.mirandaytoledo.R

@Composable
fun TopBar(modifier: Modifier = Modifier, onIconClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.top_bar_title)) },
        navigationIcon = {
            IconButton(onClick = { onIconClick() }) {
                Icon(
                    modifier = modifier.size(40.dp),
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Option Menu"
                )
            }
        }
    )
}