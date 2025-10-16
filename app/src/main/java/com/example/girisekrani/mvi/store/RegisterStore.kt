package com.example.girisekrani.mvi.store

import com.example.girisekrani.model.User
import com.example.girisekrani.mvi.intent.RegisterIntent
import com.example.girisekrani.mvi.state.RegisterUiState
import com.example.girisekrani.repository.AuthRepository
import com.example.girisekrani.util.getPasswordError
import com.example.girisekrani.util.isValidPassword
import com.example.girisekrani.util.isValidPhoneNumber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterStore(
    private val repository: AuthRepository,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
) {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun dispatch(intent: RegisterIntent, onRegisterSuccess: () -> Unit = {}) {
        when (intent) {
            is RegisterIntent.UpdateFullName -> updateFullName(intent.fullName)
            is RegisterIntent.UpdatePhoneNumber -> updatePhoneNumber(intent.phoneNumber)
            is RegisterIntent.UpdatePassword -> updatePassword(intent.password)
            is RegisterIntent.UpdateConfirmPassword -> updateConfirmPassword(intent.confirmPassword)
            is RegisterIntent.TogglePasswordVisibility -> togglePasswordVisibility()
            is RegisterIntent.ToggleConfirmPasswordVisibility -> toggleConfirmPasswordVisibility()
            is RegisterIntent.Register -> register(onRegisterSuccess)
            is RegisterIntent.ClearError -> clearError()
        }
    }

    private fun updateFullName(fullName: String) {
        _uiState.value = _uiState.value.copy(fullName = fullName, errorMessage = "")
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        val formatted = if (phoneNumber.startsWith("+90 ")) phoneNumber else "+90 " + phoneNumber.removePrefix("+90").trim()
        _uiState.value = _uiState.value.copy(phoneNumber = formatted, errorMessage = "")
    }

    private fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = "")
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword, errorMessage = "")
    }

    private fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(passwordVisible = !_uiState.value.passwordVisible)
    }

    private fun toggleConfirmPasswordVisibility() {
        _uiState.value = _uiState.value.copy(confirmPasswordVisible = !_uiState.value.confirmPasswordVisible)
    }

    private fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }

    private fun register(onSuccess: () -> Unit) {
        val state = _uiState.value
        when {
            state.fullName.trim().length < 2 -> {
                _uiState.value = state.copy(errorMessage = "Ad soyad en az 2 karakter olmalıdır")
            }
            !isValidPhoneNumber(state.phoneNumber) -> {
                _uiState.value = state.copy(errorMessage = "Geçerli telefon numarası girin")
            }
            !isValidPassword(state.password) -> {
                _uiState.value = state.copy(errorMessage = getPasswordError(state.password) ?: "Geçersiz şifre")
            }
            state.password != state.confirmPassword -> {
                _uiState.value = state.copy(errorMessage = "Şifreler eşleşmiyor")
            }
            repository.isUserExists(state.phoneNumber) -> {
                _uiState.value = state.copy(errorMessage = "Bu numara ile hesap mevcut")
            }
            else -> {
                scope.launch {
                    _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")
                    val success = try {
                        val user = User(state.fullName.trim(), state.phoneNumber, state.password)
                        repository.saveUser(user)
                    } catch (e: Exception) {
                        false
                    }
                    if (success) {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        onSuccess()
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Kayıt sırasında bir hata oluştu"
                        )
                    }
                }
            }
        }
    }
}



