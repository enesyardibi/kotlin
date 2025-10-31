package com.example.girisekrani.domain.usecase

import com.example.girisekrani.domain.repository.AuthRepository

class LoginUser(private val repository: AuthRepository) {
    operator fun invoke(phoneNumber: String, password: String): Boolean =
        repository.authenticateUser(phoneNumber, password)
}


