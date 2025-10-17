package com.teddy.mirandaytoledo.register.presentation.components.textfields

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.register.presentation.components.text.ArgumentText

@Composable
fun PhoneTextField(
    modifier: Modifier = Modifier,
) {
    var inputValue by rememberSaveable { mutableStateOf("") }
    ArgumentText(
        label = R.string.register_phone_text
    )
    OutlinedTextField(
        value = inputValue,
        onValueChange = { newValue ->
            if (newValue.length <= 10 && newValue.all { it.isDigit() }) {
                inputValue = newValue
            }
        },
        label = { Text(stringResource(R.string.register_phone_text)) },
        placeholder = { Text(stringResource(R.string.register_phone_placeholder)) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}