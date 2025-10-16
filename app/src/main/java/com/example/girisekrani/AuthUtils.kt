package com.example.girisekrani

import android.content.Context
import com.example.girisekrani.model.User
import com.example.girisekrani.util.*

// Bu dosya geriye uyumluluk için bırakıldı
// Yeni kodlarda repository ve util paketlerini kullanın

// Şifre validation fonksiyonları - util paketine taşındı
@Deprecated("Use com.example.girisekrani.util.isValidPassword instead")
fun isValidPassword(password: String): Boolean = com.example.girisekrani.util.isValidPassword(password)

@Deprecated("Use com.example.girisekrani.util.getPasswordError instead")
fun getPasswordError(password: String): String? = com.example.girisekrani.util.getPasswordError(password)

@Deprecated("Use com.example.girisekrani.util.isValidPhoneNumber instead")
fun isValidPhoneNumber(phoneNumber: String): Boolean = com.example.girisekrani.util.isValidPhoneNumber(phoneNumber)

@Deprecated("Use com.example.girisekrani.util.formatTurkishPhoneNumber instead")
fun formatTurkishPhoneNumber(input: String): String = com.example.girisekrani.util.formatTurkishPhoneNumber(input)

// Repository fonksiyonları - AuthRepository'ye taşındı
@Deprecated("Use AuthRepository.saveUser instead")
fun saveUser(context: Context, user: User) {
    val repository = com.example.girisekrani.repository.AuthRepository(context)
    repository.saveUser(user)
}

@Deprecated("Use AuthRepository.isUserExists instead")
fun isUserExists(context: Context, phoneNumber: String): Boolean {
    val repository = com.example.girisekrani.repository.AuthRepository(context)
    return repository.isUserExists(phoneNumber)
}

@Deprecated("Use AuthRepository.authenticateUser instead")
fun authenticateUser(context: Context, phoneNumber: String, password: String): Boolean {
    val repository = com.example.girisekrani.repository.AuthRepository(context)
    return repository.authenticateUser(phoneNumber, password)
}

@Deprecated("Use AuthRepository.verifyUserIdentity instead")
fun verifyUserIdentity(context: Context, phoneNumber: String, fullName: String): Boolean {
    val repository = com.example.girisekrani.repository.AuthRepository(context)
    return repository.verifyUserIdentity(phoneNumber, fullName)
}

@Deprecated("Use AuthRepository.getCurrentPassword instead")
fun getCurrentPassword(context: Context, phoneNumber: String): String? {
    val repository = com.example.girisekrani.repository.AuthRepository(context)
    return repository.getCurrentPassword(phoneNumber)
}

@Deprecated("Use AuthRepository.updateUserPassword instead")
fun updateUserPassword(context: Context, phoneNumber: String, newPassword: String): Boolean {
    val repository = com.example.girisekrani.repository.AuthRepository(context)
    return repository.updateUserPassword(phoneNumber, newPassword)
}

@Deprecated("Debug function - will be removed")
fun debugListAllUsers(context: Context) {
    // Bu fonksiyon artık gerekli değil
}