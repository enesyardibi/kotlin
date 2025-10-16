package com.example.girisekrani.mvi.store

import com.example.girisekrani.mvi.intent.ForgotPasswordIntent
import com.example.girisekrani.mvi.state.ForgotPasswordUiState
import com.example.girisekrani.repository.AuthRepository
import com.example.girisekrani.util.isValidPassword
import com.example.girisekrani.util.isValidPhoneNumber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordStore(
    private val repository: AuthRepository,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
) {
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun dispatch(intent: ForgotPasswordIntent, onPasswordResetSuccess: () -> Unit = {}) {
        when (intent) {
            is ForgotPasswordIntent.UpdatePhoneNumber -> updatePhoneNumber(intent.phoneNumber)
            is ForgotPasswordIntent.UpdateFullName -> updateFullName(intent.fullName)
            is ForgotPasswordIntent.UpdateNewPassword -> updateNewPassword(intent.password)
            is ForgotPasswordIntent.UpdateConfirmPassword -> updateConfirmPassword(intent.password)
            is ForgotPasswordIntent.TogglePasswordVisibility -> togglePasswordVisibility()
            is ForgotPasswordIntent.ToggleConfirmPasswordVisibility -> toggleConfirmPasswordVisibility()
            is ForgotPasswordIntent.VerifyIdentity -> verifyIdentity()
            is ForgotPasswordIntent.UpdatePassword -> updatePassword(onPasswordResetSuccess)
            is ForgotPasswordIntent.ClearError -> clearError()
        }
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        val formatted = if (phoneNumber.startsWith("+90 ")) phoneNumber else "+90 " + phoneNumber.removePrefix("+90").trim()
        _uiState.value = _uiState.value.copy(phoneNumber = formatted, errorMessage = "")
    }

    private fun updateFullName(fullName: String) {
        _uiState.value = _uiState.value.copy(fullName = fullName, errorMessage = "")
    }

    private fun updateNewPassword(password: String) {
        _uiState.value = _uiState.value.copy(newPassword = password, errorMessage = "")
    }

    private fun updateConfirmPassword(password: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = password, errorMessage = "")
    }

    private fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(passwordVisible = !_uiState.value.passwordVisible)
    }

    private fun toggleConfirmPasswordVisibility() {
        _uiState.value = _uiState.value.copy(confirmPasswordVisible = !_uiState.value.confirmPasswordVisible)
    }

    private fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = "", successMessage = "")
    }

    private fun verifyIdentity() {
        val state = _uiState.value
        if (!isValidPhoneNumber(state.phoneNumber) || state.fullName.trim().isEmpty()) {
            _uiState.value = state.copy(errorMessage = "Lütfen geçerli telefon numarası ve ad soyad girin")
            return
        }
        scope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "", successMessage = "")
            val verified = try {
                repository.verifyUserIdentity(state.phoneNumber, state.fullName)
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

    private fun updatePassword(onSuccess: () -> Unit) {
        val state = _uiState.value
        when {
            !isValidPassword(state.newPassword) -> {
                _uiState.value = state.copy(errorMessage = "Şifre gereksinimlerini karşılamıyor")
            }
            state.newPassword != state.confirmPassword -> {
                _uiState.value = state.copy(errorMessage = "Şifreler eşleşmiyor")
            }
            repository.getCurrentPassword(state.phoneNumber) == state.newPassword -> {
                _uiState.value = state.copy(errorMessage = "Yeni şifre eskisiyle aynı olamaz")
            }
            else -> {
                scope.launch {
                    _uiState.value = state.copy(isLoading = true, errorMessage = "", successMessage = "")
                    val updated = try {
                        repository.updateUserPassword(state.phoneNumber, state.newPassword)
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



