package com.example.girisekrani.mvi.intent

sealed class LoginIntent {
    data class UpdatePhoneNumber(val phoneNumber: String) : LoginIntent()
    data class UpdatePassword(val password: String) : LoginIntent()
    object TogglePasswordVisibility : LoginIntent()
    object Login : LoginIntent()
    object ClearError : LoginIntent()
}

