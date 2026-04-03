package com.teddy.mirandaytoledo.core.presentation.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

fun openWhatsAppWithMessage(
    context: Context,
    phone: String?,
    message: String
): Boolean {
    val normalizedPhone = phone?.filter(Char::isDigit).orEmpty()
    if (normalizedPhone.isBlank()) return false

    val uri = Uri.parse("https://wa.me/$normalizedPhone?text=${Uri.encode(message)}")
    val directIntent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.whatsapp")
    }

    return try {
        context.startActivity(directIntent)
        true
    } catch (_: ActivityNotFoundException) {
        return try {
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            true
        } catch (_: ActivityNotFoundException) {
            false
        }
    }
}
