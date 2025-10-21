package com.teddy.mirandaytoledo.register.presentation.components.checkboxes

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.register.presentation.components.text.ArgumentText

@Composable
fun DigitalFilterCheckBox(modifier: Modifier = Modifier) {

    var checked by remember { mutableStateOf(false) }

    ArgumentText(
        label = R.string.register_filter_checkbox
    )
    RadioButton(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .clip(shape = RoundedCornerShape(100)),
        selected = checked,
        onClick = { checked = !checked }
    )

}

@PreviewLightDark
@Composable
fun DigitalFilterCheckBoxPreview(modifier: Modifier = Modifier) {
    DigitalFilterCheckBox()
}