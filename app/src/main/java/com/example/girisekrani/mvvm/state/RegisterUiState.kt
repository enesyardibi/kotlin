package com.example.girisekrani.mvvm.state

data class RegisterUiState(
    val fullName: String = "",
    val phoneNumber: String = "+90 ",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)


