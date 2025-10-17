package com.teddy.mirandaytoledo.register.presentation.subpages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.register.presentation.components.selectors.DatePickerSelector
import com.teddy.mirandaytoledo.register.presentation.components.selectors.EducationalLevelSelector
import com.teddy.mirandaytoledo.register.presentation.components.selectors.GradeSelector
import com.teddy.mirandaytoledo.register.presentation.components.selectors.GroupSelector
import com.teddy.mirandaytoledo.register.presentation.components.selectors.SchoolSelector
import com.teddy.mirandaytoledo.register.presentation.components.text.ArgumentText
import com.teddy.mirandaytoledo.register.presentation.components.textfields.CategoryTextField
import com.teddy.mirandaytoledo.register.presentation.components.textfields.CategoryTextFieldWithOutText
import com.teddy.mirandaytoledo.register.presentation.components.textfields.PhoneTextField

@Composable
fun RegisterPersonalData(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(bottom = 12.dp)
            
    ) {
        EducationalLevelSelector()
        SchoolSelector()
        CategoryTextField(
            label = R.string.register_name_textfield,
            placeHolder = R.string.register_name_placeholder
        )

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            CategoryTextFieldWithOutText(
                modifier = modifier.weight(1f),
                label = R.string.register_flastname_textfield,
                placeHolder = R.string.register_flastname_placeholder
            )
            CategoryTextFieldWithOutText(
                modifier = modifier.weight(1f),
                label = R.string.register_slastname_textfield,
                placeHolder = R.string.register_slastname_placeholder
            )
        }
        ArgumentText(
            label = R.string.register_gradeandgroup_text
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            Box(modifier = Modifier.weight(1f)) {
                GradeSelector()
            }
            Box(modifier = Modifier.weight(1f)) {
                GroupSelector()
            }
        }

        CategoryTextField(
            label = R.string.register_tutor_textfield,
            placeHolder = R.string.register_tutor_placeholder
        )

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            CategoryTextFieldWithOutText(
                modifier = modifier.weight(1f),
                label = R.string.register_flastname_textfield,
                placeHolder = R.string.register_tflastname_placeholder
            )
            CategoryTextFieldWithOutText(
                modifier = modifier.weight(1f),
                label = R.string.register_slastname_textfield,
                placeHolder = R.string.register_tslastname_placeholder
            )
        }
        PhoneTextField()
        DatePickerSelector { }
    }
}

@Composable
fun RegisterPersonalDataHeader(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(all = 8.dp),
        text = "Informacion de la escuela",
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
    )
    Text(
        modifier = modifier.padding(horizontal = 8.dp),
        text = "Todos los campos son obligatorios",
        color = MaterialTheme.colorScheme.secondary
    )
}