package com.example.girisekrani.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.girisekrani.domain.model.User
import com.example.girisekrani.domain.repository.AuthRepository as AuthRepositoryContract

class AuthRepository(private val context: Context) : AuthRepositoryContract {
    private val sharedPrefs: SharedPreferences by lazy {
        context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    }

    private fun normalizePhoneNumber(phoneNumber: String): String {
        val cleanPhone = phoneNumber.replace(Regex("[^\\d+]"), "")
        return when {
            cleanPhone.startsWith("+90") && cleanPhone.length == 13 -> cleanPhone
            cleanPhone.startsWith("90") && cleanPhone.length == 12 -> "+$cleanPhone"
            cleanPhone.startsWith("5") && cleanPhone.length == 10 -> "+90$cleanPhone"
            cleanPhone.startsWith("0") && cleanPhone.length == 11 -> "+9$cleanPhone"
            else -> {
                val digitsOnly = cleanPhone.replace("+", "").replaceFirst("90", "")
                if (digitsOnly.startsWith("5") && digitsOnly.length == 10) {
                    "+90$digitsOnly"
                } else {
                    "+90$cleanPhone"
                }
            }
        }
    }

    override fun saveUser(user: User): Boolean {
        return try {
            val normalizedPhone = normalizePhoneNumber(user.phoneNumber)
            sharedPrefs.edit().apply {
                putString("${normalizedPhone}_name", user.fullName)
                putString("${normalizedPhone}_password", user.password)
                apply()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun isUserExists(phoneNumber: String): Boolean {
        val normalizedPhone = normalizePhoneNumber(phoneNumber)
        return sharedPrefs.contains("${normalizedPhone}_password") ||
               sharedPrefs.contains("${phoneNumber}_password")
    }

    override fun authenticateUser(phoneNumber: String, password: String): Boolean {
        val normalizedPhone = normalizePhoneNumber(phoneNumber)
        val savedPassword = sharedPrefs.getString("${normalizedPhone}_password", null)
            ?: sharedPrefs.getString("${phoneNumber}_password", null)
        return savedPassword == password
    }

    override fun verifyUserIdentity(phoneNumber: String, fullName: String): Boolean {
        val normalizedPhone = normalizePhoneNumber(phoneNumber)
        val savedName = sharedPrefs.getString("${normalizedPhone}_name", null)
        val fallbackName = if (savedName == null) {
            sharedPrefs.getString("${phoneNumber}_name", null)
        } else null
        val nameToCheck = savedName ?: fallbackName
        return nameToCheck?.equals(fullName.trim(), ignoreCase = true) == true
    }

    override fun getCurrentPassword(phoneNumber: String): String? {
        val normalizedPhone = normalizePhoneNumber(phoneNumber)
        return sharedPrefs.getString("${normalizedPhone}_password", null)
            ?: sharedPrefs.getString("${phoneNumber}_password", null)
    }

    override fun updateUserPassword(phoneNumber: String, newPassword: String): Boolean {
        val normalizedPhone = normalizePhoneNumber(phoneNumber)
        val keyToUse = if (sharedPrefs.contains("${normalizedPhone}_password")) {
            "${normalizedPhone}_password"
        } else if (sharedPrefs.contains("${phoneNumber}_password")) {
            "${phoneNumber}_password"
        } else {
            return false
        }
        return sharedPrefs.edit().putString(keyToUse, newPassword).commit()
    }
}




