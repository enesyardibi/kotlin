package com.example.girisekrani.mvi.intent

sealed class ForgotPasswordIntent {
    data class UpdatePhoneNumber(val phoneNumber: String) : ForgotPasswordIntent()
    data class UpdateFullName(val fullName: String) : ForgotPasswordIntent()
    data class UpdateNewPassword(val password: String) : ForgotPasswordIntent()
    data class UpdateConfirmPassword(val password: String) : ForgotPasswordIntent()
    object TogglePasswordVisibility : ForgotPasswordIntent()
    object ToggleConfirmPasswordVisibility : ForgotPasswordIntent()
    object VerifyIdentity : ForgotPasswordIntent()
    object UpdatePassword : ForgotPasswordIntent()
    object ClearError : ForgotPasswordIntent()
}

