package com.example.girisekrani.util

// Şifre validation fonksiyonları
fun isValidPassword(password: String): Boolean {
    return password.length >= 6 && 
           password.any { it.isUpperCase() } && 
           password.any { it.isLowerCase() }
}

fun getPasswordError(password: String): String? {
    return when {
        password.length < 6 -> "Şifre en az 6 karakter olmalıdır"
        !password.any { it.isUpperCase() } -> "Şifre en az bir büyük harf içermelidir"
        !password.any { it.isLowerCase() } -> "Şifre en az bir küçük harf içermelidir"
        else -> null
    }
}

// Telefon numarası validation
fun isValidPhoneNumber(phoneNumber: String): Boolean {
    val cleanedNumber = phoneNumber.replace(Regex("\\s|-"), "")
    if (!cleanedNumber.startsWith("+90")) {
        return false
    }
    val phonePattern = Regex("^\\+905\\d{9}$")
    return cleanedNumber.matches(phonePattern)
}

// Telefon numarası formatla
fun formatTurkishPhoneNumber(input: String): String {
    val digits = input.filter { it.isDigit() }
    val prefix = "+90 "

    return when {
        digits.length >= 10 -> "$prefix(${digits.substring(0,3)}) ${digits.substring(3,6)} ${digits.substring(6,8)} ${digits.substring(8,10)}"
        digits.length >= 8 -> "$prefix(${digits.substring(0,3)}) ${digits.substring(3,6)} ${digits.substring(6,8)} ${digits.substring(8)}"
        else -> "$prefix(${digits})"
    }
}

