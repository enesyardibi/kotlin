package com.example.girisekrani.domain.usecase

import com.example.girisekrani.domain.repository.AuthRepository

class VerifyUserIdentity(private val repository: AuthRepository) {
    operator fun invoke(phoneNumber: String, fullName: String): Boolean =
        repository.verifyUserIdentity(phoneNumber, fullName)
}


