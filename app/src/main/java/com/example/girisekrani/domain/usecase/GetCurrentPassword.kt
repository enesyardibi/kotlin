package com.example.girisekrani.domain.usecase

import com.example.girisekrani.domain.repository.AuthRepository

class GetCurrentPassword(private val repository: AuthRepository) {
    operator fun invoke(phoneNumber: String): String? = repository.getCurrentPassword(phoneNumber)
}


