package com.teddy.mirandaytoledo.register.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.register.presentation.components.text.BalanceText
import com.teddy.mirandaytoledo.register.presentation.components.text.ReceiptText
import com.teddy.mirandaytoledo.ui.theme.displayFontFamily

@Composable
fun PaymentReceipt(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        BalanceText(value = "Payment Receipt")
        Row {
            Image(
                modifier = modifier
                    .size(80.dp)
                    .padding(horizontal = 10.dp),
                painter = painterResource(R.drawable.logomytcolor),
                contentDescription = "Logo"
            )
            Text(
                modifier = modifier
                    .padding(top = 24.dp),
                text = "MIRANDA & TOLEDO",
                fontFamily = displayFontFamily,
                fontSize = 26.sp,
                textAlign = TextAlign.Center
            )
        }
        ReceiptText(string = "Student's Name: ")
        ReceiptText(string = "School: ")
        ReceiptText(string = "Grade:  & Group : ")
        ReceiptText(string = "Tutor's Name: ")
        Row {
            ReceiptText(string = "Bundles's Type: ")
            ReceiptText(string = "Surfaces's Type: ")
        }
        Row {
            ReceiptText(string = "Size: ")
            ReceiptText(string = "Color: ")
        }
        ReceiptText(string = "Price: ")
        ReceiptText(string = "Down Payment: ")
        ReceiptText(string = "Outstanding Balance: ")

    }
}