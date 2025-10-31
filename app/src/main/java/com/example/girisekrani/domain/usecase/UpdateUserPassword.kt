package com.example.girisekrani.domain.usecase

import com.example.girisekrani.domain.repository.AuthRepository

class UpdateUserPassword(private val repository: AuthRepository) {
    operator fun invoke(phoneNumber: String, newPassword: String): Boolean =
        repository.updateUserPassword(phoneNumber, newPassword)
}


