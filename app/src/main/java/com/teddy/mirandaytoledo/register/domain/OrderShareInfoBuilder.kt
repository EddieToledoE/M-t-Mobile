package com.teddy.mirandaytoledo.register.domain

import java.util.Locale

object OrderShareInfoBuilder {
    fun build(
        orderId: Int,
        phone: String?,
        studentName: String,
        schoolLabel: String,
        productTypeName: String,
        options: List<String>,
        photoReference: String?,
        total: Double,
        paid: Double,
        remaining: Double,
        notes: String?
    ): OrderShareInfo {
        val optionText = options.filter { it.isNotBlank() }.ifEmpty { listOf("No extra options") }
            .joinToString(" • ")

        val lines = buildList {
            add("Hello, here is your order summary from Miranda & Toledo.")
            add("")
            add("Order: #$orderId")
            add("Student: ${studentName.ifBlank { "-" }}")
            add("School: ${schoolLabel.ifBlank { "-" }}")
            add("Product: ${productTypeName.ifBlank { "-" }}")
            add("Options: $optionText")
            if (!photoReference.isNullOrBlank()) add("Photo reference: $photoReference")
            add("")
            add("Total: ${formatMoney(total)}")
            add("Paid: ${formatMoney(paid)}")
            add("Remaining: ${formatMoney(remaining)}")
            if (!notes.isNullOrBlank()) {
                add("")
                add("Notes: $notes")
            }
            add("")
            add("Thank you.")
        }

        return OrderShareInfo(
            orderId = orderId,
            phone = phone?.filter(Char::isDigit)?.takeIf { it.isNotBlank() },
            message = lines.joinToString("\n")
        )
    }

    private fun formatMoney(value: Double): String {
        return String.format(Locale.US, "$%.2f", value)
    }
}
