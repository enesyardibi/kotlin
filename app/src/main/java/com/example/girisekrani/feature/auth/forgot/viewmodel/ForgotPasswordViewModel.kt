package com.example.girisekrani.feature.auth.forgot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.girisekrani.feature.auth.forgot.viewmodel.state.ForgotPasswordUiState
import com.example.girisekrani.domain.usecase.GetCurrentPassword
import com.example.girisekrani.domain.usecase.UpdateUserPassword
import com.example.girisekrani.domain.usecase.VerifyUserIdentity
import com.example.girisekrani.core.util.isValidPassword
import com.example.girisekrani.core.util.isValidPhoneNumber
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val verifyUserIdentity: VerifyUserIdentity,
    private val getCurrentPassword: GetCurrentPassword,
    private val updateUserPassword: UpdateUserPassword
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun updatePhoneNumber(phoneNumber: String) {
        val formatted = if (phoneNumber.startsWith("+90 ")) phoneNumber else "+90 " + phoneNumber.removePrefix("+90").trim()
        _uiState.value = _uiState.value.copy(phoneNumber = formatted, errorMessage = "", successMessage = "")
    }

    fun updateFullName(fullName: String) {
        _uiState.value = _uiState.value.copy(fullName = fullName, errorMessage = "", successMessage = "")
    }

    fun updateNewPassword(password: String) {
        _uiState.value = _uiState.value.copy(newPassword = password, errorMessage = "", successMessage = "")
    }

    fun updateConfirmPassword(password: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = password, errorMessage = "", successMessage = "")
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(passwordVisible = !_uiState.value.passwordVisible)
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.value = _uiState.value.copy(confirmPasswordVisible = !_uiState.value.confirmPasswordVisible)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = "", successMessage = "")
    }

    fun verifyIdentity() {
        val state = _uiState.value
        if (!isValidPhoneNumber(state.phoneNumber) || state.fullName.trim().isEmpty()) {
            _uiState.value = state.copy(errorMessage = "Lütfen geçerli telefon numarası ve ad soyad girin")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "", successMessage = "")
            val verified = try {
                verifyUserIdentity(state.phoneNumber, state.fullName)
            } catch (e: Exception) {
                false
            }
            if (verified) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    showNewPasswordFields = true,
                    successMessage = "Kimlik doğrulandı!"
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Telefon numarası veya ad soyad bilgileri eşleşmiyor. Lütfen kayıt sırasında kullandığınız bilgileri girin."
                )
            }
        }
    }

    fun updatePassword(onSuccess: () -> Unit) {
        val state = _uiState.value
        when {
            !isValidPassword(state.newPassword) -> {
                _uiState.value = state.copy(errorMessage = "Şifre gereksinimlerini karşılamıyor")
            }
            state.newPassword != state.confirmPassword -> {
                _uiState.value = state.copy(errorMessage = "Şifreler eşleşmiyor")
            }
            getCurrentPassword(state.phoneNumber) == state.newPassword -> {
                _uiState.value = state.copy(errorMessage = "Yeni şifre eskisiyle aynı olamaz")
            }
            else -> {
                viewModelScope.launch {
                    _uiState.value = state.copy(isLoading = true, errorMessage = "", successMessage = "")
                    val updated = try {
                        updateUserPassword(state.phoneNumber, state.newPassword)
                    } catch (e: Exception) { false }
                    if (updated) {
                        _uiState.value = state.copy(isLoading = false, successMessage = "Şifre güncellendi!")
                        delay(2000)
                        onSuccess()
                    } else {
                        _uiState.value = state.copy(isLoading = false, errorMessage = "Şifre güncellenirken bir hata oluştu")
                    }
                }
            }
        }
    }
}



