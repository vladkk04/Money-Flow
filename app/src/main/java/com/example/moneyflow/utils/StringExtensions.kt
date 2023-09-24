package com.example.moneyflow.utils

import java.text.DecimalFormat


fun getFormatAmount(amount: String?): Double? {
    val amountWithoutComma = amount?.replace(",", "")
    return amountWithoutComma?.toDoubleOrNull()
}

// TODO: Make this method better and maybe rewrite(ASAP)
fun setFormatAmount(amount: String?): String? {
    with(DecimalFormat(Constants.patternFormatAmount)) {
        maximumIntegerDigits = 10
        return try {
            if (amount.isNullOrEmpty() || amount.indexOf('.') == 0) throw Exception()
            val amountWithoutComma = amount.replace(",", "")
            val parts = amountWithoutComma.split('.')
            if (amountWithoutComma.contains('.')) {
                if (parts.component2() == "0") {
                    return format(amountWithoutComma.toDouble()) + '.' + parts.component2()
                }

                if (parts.component2().isEmpty()) {
                    isDecimalSeparatorAlwaysShown = true
                }

                if (amountWithoutComma.split('.').component2().length > 2) {
                val result = amountWithoutComma.substring(0, amountWithoutComma.length - 1)
                return format(result.toDouble())
            }
                if (parts.component2().indexOf("0") == 1) {
                    return format(amountWithoutComma.toDouble()) + '0'
                }
            }
            return format(amountWithoutComma.toDouble())
        } catch (_: Exception) {
            ""
        }
    }
}
