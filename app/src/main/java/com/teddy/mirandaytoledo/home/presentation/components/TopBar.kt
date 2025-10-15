@file:OptIn(ExperimentalMaterial3Api::class)

package com.teddy.mirandaytoledo.home.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.teddy.mirandaytoledo.R

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.top_bar_title)) },
        navigationIcon = {
            Icon(
                modifier = modifier.size(40.dp),
                painter = painterResource(R.drawable.logologin),
                contentDescription = ""
            )
        }
    )
}