@file:Deprecated("Moved to com.example.girisekrani.core.util", ReplaceWith("com.example.girisekrani.core.util"))
package com.example.girisekrani.util

// Şifre validation fonksiyonları
fun isValidPassword(password: String): Boolean = com.example.girisekrani.core.util.isValidPassword(password)

fun getPasswordError(password: String): String? = com.example.girisekrani.core.util.getPasswordError(password)

// Telefon numarası validation
fun isValidPhoneNumber(phoneNumber: String): Boolean = com.example.girisekrani.core.util.isValidPhoneNumber(phoneNumber)

// Telefon numarası formatla
fun formatTurkishPhoneNumber(input: String): String = com.example.girisekrani.core.util.formatTurkishPhoneNumber(input)

