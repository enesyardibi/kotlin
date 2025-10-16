package com.example.girisekrani.feature.auth.register.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.girisekrani.core.util.getPasswordError
import com.example.girisekrani.core.util.isValidPassword
import com.example.girisekrani.core.util.isValidPhoneNumber
import com.example.girisekrani.feature.auth.register.presentation.RegisterViewModel
import com.example.girisekrani.data.repository.AuthRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit = {}, onBackToLogin: () -> Unit = {}) {
    val context = LocalContext.current
    val vm: RegisterViewModel = viewModel(
        factory = viewModelFactory {
            initializer { RegisterViewModel(AuthRepository(context)) }
        }
    )
    val uiState by vm.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFF667eea), Color(0xFF764ba2)))
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Başlık
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Hesap Oluşturun", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C3E50))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Yeni hesabınızı oluşturun", fontSize = 16.sp, color = Color(0xFF7F8C8D))
                }
            }

            // Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Ad Soyad
                    OutlinedTextField(
                        value = uiState.fullName,
                        onValueChange = { 
                            vm.updateFullName(it)
                        },
                        label = { Text("Ad Soyad") },
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = uiState.fullName.trim().length < 2 && uiState.fullName.isNotEmpty(),
                        singleLine = true,
                        maxLines = 1
                    )

                    // Telefon
                    OutlinedTextField(
                        value = uiState.phoneNumber,
                        onValueChange = { 
                            vm.updatePhoneNumber(it)
                        },
                        label = { Text("Telefon Numarası") },
                        placeholder = { Text("5XX XXX XX XX") },
                        leadingIcon = { Icon(Icons.Default.Phone, null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = !isValidPhoneNumber(uiState.phoneNumber) && uiState.phoneNumber.length > 4,
                        singleLine = true,
                        maxLines = 1
                    )

                    // Şifre
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = { 
                            vm.updatePassword(it)
                        },
                        label = { Text("Şifre") },
                        placeholder = { Text("En az 6 karakter, büyük/küçük harf") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        trailingIcon = {
                            IconButton(onClick = { 
                                vm.togglePasswordVisibility()
                            }) {
                                Icon(if (uiState.passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                            }
                        },
                        visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = !isValidPassword(uiState.password) && uiState.password.isNotEmpty(),
                        singleLine = true,
                        maxLines = 1
                    )
                    
                    // Şifre hata mesajı
                    getPasswordError(uiState.password)?.let { error ->
                        if (uiState.password.isNotEmpty()) {
                            Text(
                                text = error,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }

                    // Şifre Tekrar
                    OutlinedTextField(
                        value = uiState.confirmPassword,
                        onValueChange = { 
                            vm.updateConfirmPassword(it)
                        },
                        label = { Text("Şifre Tekrar") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        trailingIcon = {
                            IconButton(onClick = { 
                                vm.toggleConfirmPasswordVisibility()
                            }) {
                                Icon(if (uiState.confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                            }
                        },
                        visualTransformation = if (uiState.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = uiState.password != uiState.confirmPassword && uiState.confirmPassword.isNotEmpty(),
                        singleLine = true,
                        maxLines = 1
                    )

                    // Error
                    if (uiState.errorMessage.isNotEmpty()) {
                        Text(
                            text = uiState.errorMessage, 
                            color = Color.Red, 
                            fontSize = 14.sp, 
                            textAlign = TextAlign.Center, 
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Kayıt Butonu
                    Button(
                        onClick = {
                            vm.register(onRegisterSuccess)
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        enabled = !uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF667eea)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Text("Hesap Oluştur", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }
                    }

                    // Geri Dön
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text("Zaten hesabınız var mı? ", color = Color(0xFF7F8C8D), fontSize = 14.sp)
                        Text(
                            "Giriş Yapın", 
                            color = Color(0xFF667eea), 
                            fontSize = 14.sp, 
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { onBackToLogin() }
                        )
                    }
                }
            }
        }
    }
}


