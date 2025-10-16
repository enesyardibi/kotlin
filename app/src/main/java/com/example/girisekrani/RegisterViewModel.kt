package com.example.girisekrani

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.girisekrani.domain.model.User
import com.example.girisekrani.mvvm.state.RegisterUiState
import com.example.girisekrani.data.repository.AuthRepository
import com.example.girisekrani.core.util.getPasswordError
import com.example.girisekrani.core.util.isValidPassword
import com.example.girisekrani.core.util.isValidPhoneNumber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun updateFullName(fullName: String) {
        _uiState.value = _uiState.value.copy(fullName = fullName, errorMessage = "")
    }

    fun updatePhoneNumber(phoneNumber: String) {
        val formatted = if (phoneNumber.startsWith("+90 ")) phoneNumber else "+90 " + phoneNumber.removePrefix("+90").trim()
        _uiState.value = _uiState.value.copy(phoneNumber = formatted, errorMessage = "")
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = "")
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword, errorMessage = "")
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(passwordVisible = !_uiState.value.passwordVisible)
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.value = _uiState.value.copy(confirmPasswordVisible = !_uiState.value.confirmPasswordVisible)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }

    fun register(onSuccess: () -> Unit) {
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
                viewModelScope.launch {
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


