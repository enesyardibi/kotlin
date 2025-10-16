package com.example.girisekrani.mvi.store

import com.example.girisekrani.mvi.intent.LoginIntent
import com.example.girisekrani.mvi.state.LoginUiState
import com.example.girisekrani.repository.AuthRepository
import com.example.girisekrani.util.isValidPhoneNumber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginStore(
    private val repository: AuthRepository,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
) {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun dispatch(intent: LoginIntent, onLoginSuccess: () -> Unit = {}) {
        when (intent) {
            is LoginIntent.UpdatePhoneNumber -> updatePhoneNumber(intent.phoneNumber)
            is LoginIntent.UpdatePassword -> updatePassword(intent.password)
            is LoginIntent.TogglePasswordVisibility -> togglePasswordVisibility()
            is LoginIntent.Login -> login(onLoginSuccess)
            is LoginIntent.ClearError -> clearError()
        }
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        val formatted = if (phoneNumber.startsWith("+90 ")) phoneNumber else "+90 " + phoneNumber.removePrefix("+90").trim()
        _uiState.value = _uiState.value.copy(
            phoneNumber = formatted,
            isPhoneValid = isValidPhoneNumber(formatted),
            errorMessage = ""
        )
    }

    private fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            errorMessage = ""
        )
    }

    private fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            passwordVisible = !_uiState.value.passwordVisible
        )
    }

    private fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }

    private fun login(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (!isValidPhoneNumber(state.phoneNumber)) {
            _uiState.value = state.copy(errorMessage = "Geçerli telefon numarası girin")
            return
        }
        if (state.password.isEmpty()) {
            _uiState.value = state.copy(errorMessage = "Şifre boş olamaz")
            return
        }

        scope.launch {
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



