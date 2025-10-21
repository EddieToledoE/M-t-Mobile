package com.teddy.mirandaytoledo.register.presentation.subpages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.register.presentation.components.PaymentReceipt
import com.teddy.mirandaytoledo.register.presentation.components.text.BalanceText
import com.teddy.mirandaytoledo.register.presentation.components.textfields.CategoryTextField

@Composable
fun RegisterPaymentData(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(bottom = 12.dp))
    {
        BalanceText(value = "Final Balance $2000")
        CategoryTextField(
            modifier = modifier.height(100.dp),
            label = R.string.register_observations_textfield,
            placeHolder = R.string.register_observations_placeholder
        )
        CategoryTextField(
            label = R.string.register_downpayment_textfield,
            placeHolder = R.string.register_downpayment_placeholder
        )
        CategoryTextField(
            label = R.string.register_additional_textfield,
            placeHolder = R.string.register_additional_placeholder
        )
        BalanceText(value = "Outstanding balance $2000")
        HorizontalDivider(
            modifier = modifier
                .height(24.dp)
                .padding(horizontal = 10.dp, vertical = 1.5.dp),
            thickness = 4.dp,
            color = MaterialTheme.colorScheme.secondary
        )
        PaymentReceipt()
    }
}

@Composable
fun RegisterPaymentDataHeader(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(all = 8.dp),
        text = stringResource(R.string.register_paymentdata_header),
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
    )
    Text(
        modifier = modifier.padding(horizontal = 8.dp),
        text = stringResource(R.string.register_paymentdata_subtitle),
        color = MaterialTheme.colorScheme.secondary
    )
}