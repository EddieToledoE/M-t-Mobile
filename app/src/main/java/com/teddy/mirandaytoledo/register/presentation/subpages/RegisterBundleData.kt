package com.teddy.mirandaytoledo.register.presentation.subpages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.register.presentation.components.textfields.CategoryTextField

@Composable
fun RegisterBundleData(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(bottom = 12.dp))
    {
        CategoryTextField(
            label = R.string.register_gown_textfield,
            placeHolder = R.string.register_gown_placeholder
        )
    }
}

@Composable
fun RegisterBundleDataHeader(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(all = 8.dp),
        text = stringResource(R.string.register_bundledata_header),
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
    )
    Text(
        modifier = modifier.padding(horizontal = 8.dp),
        text = stringResource(R.string.register_personaldata_subtitle),
        color = MaterialTheme.colorScheme.secondary
    )
}