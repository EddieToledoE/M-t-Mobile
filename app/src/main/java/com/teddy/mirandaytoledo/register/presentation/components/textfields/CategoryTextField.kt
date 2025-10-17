package com.teddy.mirandaytoledo.register.presentation.components.textfields

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teddy.mirandaytoledo.register.presentation.components.text.ArgumentText

@Composable
fun CategoryTextField(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    @StringRes placeHolder: Int
) {
    var inputValue by rememberSaveable { mutableStateOf("") }
    ArgumentText(
        label = label
    )
    OutlinedTextField(
        value = inputValue,
        onValueChange = { inputValue = it },
        label = { Text(stringResource(label)) },
        placeholder = { Text(stringResource(placeHolder)) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    )
}

@Composable
fun CategoryTextFieldWithOutText(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    @StringRes placeHolder: Int
) {
    var inputValue by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        value = inputValue,
        onValueChange = { inputValue = it },
        label = { Text(stringResource(label)) },
        placeholder = { Text(stringResource(placeHolder)) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    )
}
