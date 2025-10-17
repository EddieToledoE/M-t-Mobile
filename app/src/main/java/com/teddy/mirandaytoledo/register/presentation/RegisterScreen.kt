package com.teddy.mirandaytoledo.register.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teddy.mirandaytoledo.core.navigation.Navigator
import com.teddy.mirandaytoledo.core.presentation.components.MainScaffold
import com.teddy.mirandaytoledo.register.presentation.components.RegisterStatus
import com.teddy.mirandaytoledo.register.presentation.subpages.RegisterPersonalData
import com.teddy.mirandaytoledo.register.presentation.subpages.RegisterPersonalDataHeader
import com.teddy.mirandaytoledo.ui.theme.MirandaytoledoTheme

@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 10.dp)
            .border(
                width = 3.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                shape = RoundedCornerShape(size = 16.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start

    ) {
        RegisterStatus()
        RegisterPersonalDataHeader()
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            RegisterPersonalData()
        }
        Row(
            modifier = modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .padding(vertical = 3.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedButton(onClick = {}, modifier = modifier.weight(0.4f).padding(horizontal = 4.dp)) { Text("Back") }
            Button(onClick = {}, modifier = modifier.weight(0.6f).padding(horizontal = 4.dp)) { Text("Next") }

        }


    }
}

@Composable
@PreviewLightDark
fun RegisterScreenPreview() {
    MirandaytoledoTheme {
        MainScaffold({}, Navigator()) {
            RegisterScreen()
        }
    }
}