package com.example.girisekrani.feature.auth.login.viewmodel.state

data class LoginUiState(
    val phoneNumber: String = "+90 ",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isPhoneValid: Boolean = true
)



