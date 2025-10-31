package com.example.girisekrani.domain.repository

import com.example.girisekrani.domain.model.User

interface AuthRepository {
    fun saveUser(user: User): Boolean
    fun isUserExists(phoneNumber: String): Boolean
    fun authenticateUser(phoneNumber: String, password: String): Boolean
    fun verifyUserIdentity(phoneNumber: String, fullName: String): Boolean
    fun getCurrentPassword(phoneNumber: String): String?
    fun updateUserPassword(phoneNumber: String, newPassword: String): Boolean
}


