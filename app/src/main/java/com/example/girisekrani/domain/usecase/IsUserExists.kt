package com.example.girisekrani.domain.usecase

import com.example.girisekrani.domain.repository.AuthRepository

class IsUserExists(private val repository: AuthRepository) {
    operator fun invoke(phoneNumber: String): Boolean = repository.isUserExists(phoneNumber)
}


