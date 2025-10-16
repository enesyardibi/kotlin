package com.example.girisekrani.mvi.intent

sealed class RegisterIntent {
    data class UpdateFullName(val fullName: String) : RegisterIntent()
    data class UpdatePhoneNumber(val phoneNumber: String) : RegisterIntent()
    data class UpdatePassword(val password: String) : RegisterIntent()
    data class UpdateConfirmPassword(val confirmPassword: String) : RegisterIntent()
    object TogglePasswordVisibility : RegisterIntent()
    object ToggleConfirmPasswordVisibility : RegisterIntent()
    object Register : RegisterIntent()
    object ClearError : RegisterIntent()
}

