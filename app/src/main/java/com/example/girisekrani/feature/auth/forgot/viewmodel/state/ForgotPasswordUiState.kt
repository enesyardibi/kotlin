package com.example.girisekrani.feature.auth.forgot.viewmodel.state

data class ForgotPasswordUiState(
    val phoneNumber: String = "+90 ",
    val fullName: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = "",
    val showNewPasswordFields: Boolean = false
)



