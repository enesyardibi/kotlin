package com.example.girisekrani.domain.usecase

import com.example.girisekrani.domain.model.User
import com.example.girisekrani.domain.repository.AuthRepository

class RegisterUser(private val repository: AuthRepository) {
    operator fun invoke(user: User): Boolean = repository.saveUser(user)
}


