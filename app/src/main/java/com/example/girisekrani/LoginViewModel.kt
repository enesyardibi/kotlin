package com.example.girisekrani

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.girisekrani.mvvm.state.LoginUiState
import com.example.girisekrani.repository.AuthRepository
import com.example.girisekrani.util.isValidPhoneNumber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updatePhoneNumber(phoneNumber: String) {
        val formatted = if (phoneNumber.startsWith("+90 ")) phoneNumber else "+90 " + phoneNumber.removePrefix("+90").trim()
        _uiState.value = _uiState.value.copy(
            phoneNumber = formatted,
            isPhoneValid = isValidPhoneNumber(formatted),
            errorMessage = ""
        )
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = "")
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(passwordVisible = !_uiState.value.passwordVisible)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }

    fun login(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (!isValidPhoneNumber(state.phoneNumber)) {
            _uiState.value = state.copy(errorMessage = "Geçerli telefon numarası girin")
            return
        }
        if (state.password.isEmpty()) {
            _uiState.value = state.copy(errorMessage = "Şifre boş olamaz")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")
            val success = try {
                repository.authenticateUser(state.phoneNumber, state.password)
            } catch (e: Exception) {
                false
            }
            if (success) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Telefon veya şifre hatalı"
                )
            }
        }
    }
}


